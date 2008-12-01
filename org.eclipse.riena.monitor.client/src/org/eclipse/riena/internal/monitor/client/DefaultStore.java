/*******************************************************************************
 * Copyright (c) 2007, 2008 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.monitor.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.util.IOUtils;
import org.eclipse.riena.core.util.Literal;
import org.eclipse.riena.core.util.PropertiesUtils;
import org.eclipse.riena.monitor.client.IStore;
import org.eclipse.riena.monitor.common.Collectible;
import org.osgi.service.log.LogService;

/**
 * TODO config
 */
public class DefaultStore implements IStore, IExecutableExtension {

	private File storeFolder;
	private int maxItems;

	private final ScheduledExecutorService storeExecutor;

	private static final String TRANSFER_FILE_EXTENSION = ".trans"; //$NON-NLS-1$
	private static final String COLLECT_FILE_EXTENSION = ".coll"; //$NON-NLS-1$
	private static final String DEL_FILE_EXTENSION = ".del"; //$NON-NLS-1$
	private static final String CATEGORY_DELIMITER = "#"; //$NON-NLS-1$

	private static final String MAX_ITEMS = "maxItems"; //$NON-NLS-1$

	private static final Logger LOGGER = Activator.getDefault().getLogger(DefaultStore.class);

	public DefaultStore() {
		//		System.out.println("Platform.getConfigurationLocation: " + Platform.getConfigurationLocation().getURL());
		//		System.out.println("Platform.getInstallLocation: " + Platform.getInstallLocation().getURL());
		//		System.out.println("Platform.getInstanceLocation: " + Platform.getInstanceLocation().getURL());
		//		System.out.println("Platform.getLocation: " + Platform.getLocation());
		//		System.out.println("Platform.getLogFileLocation: " + Platform.getLogFileLocation());
		//		System.out.println("Platform.getUserLocation: " + Platform.getUserLocation().getURL());

		// TODO What is the best place to store the stuff??
		storeFolder = new File(Platform.getUserLocation().getURL().getFile(), ".collectiblestore");
		storeFolder.mkdirs();
		Assert.isTrue(storeFolder.exists());
		Assert.isTrue(storeFolder.isDirectory());
		LOGGER.log(LogService.LOG_DEBUG, "DefaultStore at " + storeFolder); //$NON-NLS-1$
		storeExecutor = Executors.newSingleThreadScheduledExecutor();
	}

	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {
		Map<String, String> properties = null;
		try {
			properties = PropertiesUtils.asMap(data, Literal.map(MAX_ITEMS, "100")); //$NON-NLS-1$
		} catch (IllegalArgumentException e) {
			throw configurationException("Bad configuration.", e); //$NON-NLS-1$
		}
		try {
			maxItems = Integer.valueOf(properties.get(MAX_ITEMS));
			Assert.isLegal(maxItems > 0, "maxItems must be greater than 0."); //$NON-NLS-1$
		} catch (IllegalArgumentException e) {
			throw configurationException("Bad configuration. Parsing maxItems failed.", e); //$NON-NLS-1$
		}
	}

	private CoreException configurationException(String message, Exception e) {
		return new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, e));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.IStore#close()
	 */
	public void close() {
		storeExecutor.shutdown();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.IStore#open()
	 */
	public void open() {
		storeExecutor.scheduleWithFixedDelay(new Cleaner(), 2l, 15l * 60, TimeUnit.SECONDS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.IStore#flush()
	 */
	public void flush() {
		// nothing to do here
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.internal.monitor.client.ICollectibleStore#collect(org
	 * .eclipse.riena.monitor.core.Collectible)
	 */
	public synchronized boolean collect(final Collectible<?> collectible) {
		File file = getFile(collectible, COLLECT_FILE_EXTENSION);
		putCollectible(collectible, file);

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.IStore#prepareForTransfer
	 * (java.lang.String)
	 */
	public void prepareTransferables(final String category) {
		File[] trans = storeFolder.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.startsWith(category) && name.endsWith(COLLECT_FILE_EXTENSION);
			}
		});
		for (File file : trans) {
			String name = file.getName().replace(COLLECT_FILE_EXTENSION, TRANSFER_FILE_EXTENSION);
			file.renameTo(new File(file.getParent(), name));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.IStore#getTransferables(java
	 * .lang.String)
	 */
	public synchronized List<Collectible<?>> retrieveTransferables(final String category) {
		File[] transferables = storeFolder.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.startsWith(category) && name.endsWith(TRANSFER_FILE_EXTENSION);
			}
		});
		List<Collectible<?>> collectibles = new ArrayList<Collectible<?>>();
		for (File transferable : transferables) {
			Collectible<?> collectible = getCollectible(transferable);
			if (collectible != null) {
				collectibles.add(collectible);
			}
		}
		return collectibles;
	}

	/**
	 * Get the decryptor for storing the collectibles.
	 * <p>
	 * <b>Note: </b>This hook method is intended to be overwritten to provide
	 * encrypted storage on the local file system on the client. Otherwise no
	 * encryption will be used.
	 * 
	 * @param is
	 * @return
	 */
	protected InputStream getDecryptor(InputStream is) throws IOException {
		return is;
	}

	/**
	 * 
	 * Get the encryptor for retrieving the collectibles.
	 * <p>
	 * <b>Note: </b>This hook method is intended to be overwritten to provide
	 * encrypted storage on the local file system on the client. Otherwise no
	 * encryption will be used.
	 * 
	 * @param os
	 * @return
	 */
	protected OutputStream getEncryptor(OutputStream os) throws IOException {
		return os;
	}

	/**
	 * Get the compressor for storing the collectibles.
	 * <p>
	 * <b>Note: </b>This hook method may be overwritten to provide another
	 * compressing technology. This method uses GZIP.
	 * 
	 * @param os
	 * @return
	 * @throws IOException
	 */
	protected OutputStream getCompressor(OutputStream os) throws IOException {
		return new GZIPOutputStream(os);
	}

	/**
	 * 
	 * Get the encryptor for retrieving the collectibles.
	 * <p>
	 * <b>Note: </b>This hook method is intended to be overwritten to provide
	 * encrypted storage on the local file system on the client. Otherwise no
	 * encryption will be used.
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	protected InputStream getDecompressor(InputStream is) throws IOException {
		return new GZIPInputStream(is);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.IStore#commitTransferred(
	 * java.util.List)
	 */
	public synchronized void commitTransferred(List<Collectible<?>> collectibles) {
		for (Collectible<?> collectible : collectibles) {
			delete(getFile(collectible, TRANSFER_FILE_EXTENSION));
		}
	}

	/**
	 * Try to delete the given file. If it is not deletable mark it deletable
	 * and try to delete on jvm exit.
	 * 
	 * @param file
	 */
	private void delete(File file) {
		if (!file.delete()) {
			if (file.getName().endsWith(DEL_FILE_EXTENSION)) {
				file.deleteOnExit();
				return;
			}
			File toDelete = new File(file, DEL_FILE_EXTENSION);
			file.renameTo(toDelete);
			toDelete.deleteOnExit();
		}
	}

	/**
	 * Get collectible from file.
	 * 
	 * @param file
	 * @return
	 */
	private Collectible<?> getCollectible(File file) {
		ObjectInputStream objectis = null;
		try {
			InputStream fis = new FileInputStream(file);
			InputStream decris = getDecryptor(fis);
			InputStream gzipis = getDecompressor(decris);
			objectis = new ObjectInputStream(gzipis);
			return (Collectible<?>) objectis.readObject();
		} catch (Exception e) {
			// TODO Error handling
			e.printStackTrace();
			return null;
		} finally {
			IOUtils.close(objectis);
		}
	}

	/**
	 * Store collectible into file.
	 * 
	 * @param collectible
	 * @param file
	 */
	private void putCollectible(Collectible<?> collectible, File file) {
		ObjectOutputStream objectos = null;
		try {
			OutputStream fos = new FileOutputStream(file);
			OutputStream encos = getEncryptor(fos);
			OutputStream gzipos = getCompressor(encos);
			objectos = new ObjectOutputStream(gzipos);
			objectos.writeObject(collectible);
		} catch (IOException e) {
			// TODO Error handling!!?
			e.printStackTrace();
		} finally {
			IOUtils.close(objectos);
		}
	}

	private File getFile(Collectible<?> collectible, String extension) {
		return new File(storeFolder, collectible.getCategory() + CATEGORY_DELIMITER + collectible.getUUID().toString()
				+ extension);
	}

	/**
	 * Perform cleanup of the store as defined by the configuration properties.
	 */
	public class Cleaner implements Runnable {
		public void run() {
			File[] scrutinizedFiles = storeFolder.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(COLLECT_FILE_EXTENSION) || name.endsWith(DEL_FILE_EXTENSION);
				}
			});
			Map<String, List<File>> categorizedScrutinized = new HashMap<String, List<File>>(scrutinizedFiles.length);
			for (File scrutinizedFile : scrutinizedFiles) {
				int categoryDelimiterIndex = scrutinizedFile.getName().indexOf(CATEGORY_DELIMITER);
				if (categoryDelimiterIndex == -1) {
					continue;
				}
				String category = scrutinizedFile.getName().substring(0, categoryDelimiterIndex);
				List<File> files = categorizedScrutinized.get(category);
				if (files == null) {
					files = new ArrayList<File>();
					categorizedScrutinized.put(category, files);
				}
				files.add(scrutinizedFile);
			}
			for (Map.Entry<String, List<File>> entry : categorizedScrutinized.entrySet()) {
				clean(entry.getValue());
			}
		}

		/**
		 * @param value
		 */
		private void clean(List<File> files) {
			if (files.size() < maxItems) {
				return;
			}
			Collections.sort(files, new Comparator<File>() {
				public int compare(File file1, File file2) {
					Long time1 = file1.lastModified();
					Long time2 = file2.lastModified();
					return time1.compareTo(time2);
				}
			});
			for (int i = 0; i < files.size() - maxItems; i++) {
				delete(files.get(i));
			}
		}
	}

}
