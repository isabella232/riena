package org.eclipse.riena.navigation.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.riena.navigation.INavigationAssembler;

/**
 * Defines that the buildNode() method of an {@link INavigationAssembler} should
 * be run on a worker-thread. The UI events will be dispatched, while the worker
 * is running so the UI does not freeze.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface RunAsync {

}