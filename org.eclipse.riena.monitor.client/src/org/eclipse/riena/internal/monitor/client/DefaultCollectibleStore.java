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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.util.IOUtils;
import org.eclipse.riena.monitor.client.ICollectibleStore;
import org.eclipse.riena.monitor.client.IEncrypter;
import org.eclipse.riena.monitor.common.Collectible;
import org.osgi.service.log.LogService;

/**
 * TODO config
 */
public class DefaultCollectibleStore implements ICollectibleStore {

	private IEncrypter encrypter = new DefaultEncrypter();
	private File storeFolder;

	private static final String TRANSFER_FILE_EXTENSION = ".trans"; //$NON-NLS-1$
	private static final String COLLECT_FILE_EXTENSION = ".coll"; //$NON-NLS-1$
	private static final String DEL_FILE_EXTENSION = ".del"; //$NON-NLS-1$

	private static final Logger LOGGER = Activator.getDefault().getLogger(DefaultCollectibleStore.class);

	public DefaultCollectibleStore() {
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
		LOGGER.log(LogService.LOG_DEBUG, "DefaultCollectibleStore at " + storeFolder);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.internal.monitor.client.ICollectibleStore#collect(org
	 * .eclipse.riena.monitor.core.Collectible)
	 */
	public synchronized boolean collect(final Collectible<?> collectible) {
		if (encrypter == null) {
			return false;
		}
		ObjectOutputStream out = null;
		try {
			File file = getFile(collectible, COLLECT_FILE_EXTENSION);
			FileOutputStream fos = new FileOutputStream(file);
			OutputStream enc = encrypter.getEncrypter(fos);
			GZIPOutputStream gzip = new GZIPOutputStream(enc);
			out = new ObjectOutputStream(gzip);
			out.writeObject(collectible);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtils.close(out);
		}
		if (collectible.isTriggeringTransfer()) {
			makeTransferable(collectible.getCategory());
		}
		return true;
	}

	/**
	 * @param category
	 */
	private void makeTransferable(final String category) {
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
	 * @see
	 * org.eclipse.riena.monitor.client.ICollectibleStore#getTransferables(java
	 * .lang.String)
	 */
	public synchronized List<Collectible<?>> retrieveTransferables(String category) {
		File[] transferables = storeFolder.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.endsWith(TRANSFER_FILE_EXTENSION);
			}
		});
		List<Collectible<?>> collectibles = new ArrayList<Collectible<?>>();
		for (File transferable : transferables) {
			ObjectInputStream in = null;
			try {
				FileInputStream fis = new FileInputStream(transferable);
				InputStream decr = encrypter.getDecrypter(fis);
				GZIPInputStream gzip = new GZIPInputStream(decr);
				in = new ObjectInputStream(gzip);
				Collectible<?> collectible = (Collectible<?>) in.readObject();
				collectibles.add(collectible);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				IOUtils.close(in);
			}
		}
		return collectibles;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.monitor.client.ICollectibleStore#commitTransferred(
	 * java.util.List)
	 */
	public synchronized void commitTransferred(List<Collectible<?>> collectibles) {
		for (Collectible<?> collectible : collectibles) {
			File transferred = getFile(collectible, TRANSFER_FILE_EXTENSION);
			if (!transferred.delete()) {
				File toDelete = new File(transferred, DEL_FILE_EXTENSION);
				transferred.renameTo(toDelete);
				toDelete.deleteOnExit();
			}
		}
	}

	private File getFile(Collectible<?> collectible, String extension) {
		return new File(storeFolder, collectible.getCategory() + "-" + collectible.getUUID().toString() + extension); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.ICollectibleStore#flush()
	 */
	public void flush() {
		// nothing to do here
	}
}
