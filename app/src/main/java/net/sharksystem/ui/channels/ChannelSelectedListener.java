package net.sharksystem.ui.channels;

/**
 * Listener that is informed when a channel was selected
 */
public interface ChannelSelectedListener {

    /**
     * When the item was clicked shortly
     * @param viewHolder the ViewHolder object of the channel item
     */
    void channelSelectedWithShortClick(ChannelListContentAdapter.ViewHolder viewHolder);

    /**
     * When the item was clicked for a longer time
     * @param viewHolder the ViewHolder object of the item
     */
    void channelSelectedWithLongClick(ChannelListContentAdapter.ViewHolder viewHolder);
}
