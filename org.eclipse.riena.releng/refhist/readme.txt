What is this ?
--------------

When we do a refactoring that affects many classes, like renaming an interface,
we make a refactoring history available here. 

These files can be applied to a workspace, by selecting Refactor > Apply History 
in Eclipse. If you're not seeing the Refactor menu, select a java/plug-in project 
in the Package Explorer :-).

List of Refactorings
--------------------

20081028_interface_rename.xml

Contains the following rename refactorings:
* ITextFieldRidget to ITextRidget
* INumericValueTextFieldRidget to INumericTextRidget 
* IDecimalValueTextFieldRidget to IDecimalTextRidget 
* IDateTextFieldRidget to IDateTextRidget
* IComboBoxRidget to IComboRidget
* IComboBoxEntryFactory to IComboEntryFactory
* ITreeRidget.collapseTree() to ITreeRidget.collapseAll()
* ITreeRidget.expandTree() to ITreeRidget.expandAll()
