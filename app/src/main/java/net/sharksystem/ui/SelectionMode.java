package net.sharksystem.ui;

/**
 * Mode in which a fragment with a recycler view can be, if the items can be deleted
 */
public enum SelectionMode {
    /**
     * State in which a selected item redirects to a more detailed view of the item or something
     * similar
     */
    SELECT,

    /**
     * State in which items are deleted
     */
    DELETE
}
