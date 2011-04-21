package org.eclipse.riena.security.ui.filter;

import java.security.Permission;

import org.eclipse.riena.core.injector.extension.DefaultValue;
import org.eclipse.riena.core.injector.extension.ExtensionInterface;

/**
 * Extension interface for the extension point "permissionFilterMapping"
 * 
 */
@ExtensionInterface(id = "permissionFilterMapping")
public interface IPermissionFilterMappingExtension {

	@DefaultValue(value = "org.eclipse.riena.security.navigation.filter.UserInterfacePermission")
	Class<? extends Permission> getPermissionClass();

	String getPermissionName();

	String getPermissionAction();

	String getFilterID();

}