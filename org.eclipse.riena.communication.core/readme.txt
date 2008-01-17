==========================================================================
=== Nyota Notes About Generic Class Loading and Instantiation Behavior ===
==========================================================================

The Nyota bundle content includes generic class loading and object instantiation or delegates this behavior to other
Nyota bundles. In the follow to situations Nyota load classes or instantiate objects generically.

a) a IRemoteServiceFactory creates a proxy reference (IRemoteServiceReference) by an end point description
b) a web service request or response becomes serialized or deserialized.

Usually an application bundle defines service interfaces and API types. Each bundle has their own context class 
loader. A Bundle determines which type becomes exported (visible for other bundles). A bundle "A" known about 
(exported) types only if bundle "A" is depend on an other bundle which defines or reexports a type.

The de.compeople.nyota.core bundle and other (Nyota) factory or publisher bundles are like generic factory bundles. 
They have no dependency to any application bundle.

That Nyota can work correctly it is requisite that Nyota know the external interface and API class types.
Nyota supports the Eclipse buddy concept. Application bundles should be registered to de.compeople.nyota.core 
bundle. The de.compeople.nyota.core bundle is like a "single point of buddy configuration" for all Nyota bundles.

Nyota Eclipse-BuddyPolicy sample

          ==================================================
          | Manifest.mf: de.compeople.nyota.core           |
          |------------------------------------------------|
          | ..                                             |
          | Eclipse-BuddyPolicy: registered                |
          | ..                                             |
          ==================================================
                                  |
                                 / \
                                  |
                                  | buddy
                                  |
          ==================================================
          | Manifest.mf: foo.myapplication.api             |
          |------------------------------------------------|
          | ..                                             |
          | Eclipse-RegisterBuddy: de.compeople.nyota.core |         |
          | ..                                             |
          ==================================================
