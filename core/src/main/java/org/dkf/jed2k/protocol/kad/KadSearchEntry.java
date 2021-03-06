package org.dkf.jed2k.protocol.kad;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.dkf.jed2k.exception.JED2KException;
import org.dkf.jed2k.protocol.*;
import org.dkf.jed2k.protocol.tag.Tag;

import java.nio.ByteBuffer;

/**
 * Created by inkpot on 15.11.2016.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude = "info")
public class KadSearchEntry implements Serializable, SearchEntry {
    private KadId kid = null;
    private Container<UInt8, Tag> info = Container.makeByte(Tag.class);

    public KadSearchEntry() {
        this.kid = new KadId();
    }

    public KadSearchEntry(final KadId kid) {
        this.kid = kid;
    }

    @Override
    public ByteBuffer get(ByteBuffer src) throws JED2KException {
        return info.get(kid.get(src));
    }

    @Override
    public ByteBuffer put(ByteBuffer dst) throws JED2KException {
        return info.put(kid.put(dst));
    }

    @Override
    public int bytesCount() {
        return kid.bytesCount() + info.bytesCount();
    }

    @Override
    public Hash getHash() {
        return kid;
    }

    @Override
    public int getSource() {
        return SearchEntry.SOURCE_KAD;
    }

    @Override
    public final String getFileName() {
        Tag t = Tag.getTagById(Tag.FT_FILENAME, info);
        return (t != null)?t.asStringValue():"";
    }

    @Override
    public long getFileSize() {
        long res = 0;
        Tag t = Tag.getTagById(Tag.FT_FILESIZE, info);
        if (t != null) res = t.asLongValue();
        Tag th = Tag.getTagById(Tag.FT_FILESIZE_HI, info);

        if (th != null) {
            long hi = th.asLongValue();
            hi = hi << 32;
            res += hi;
        }

        // additional code for file size extraction from KAD
        // when we can't get size from long tags and have raw tag(most likely bsob) for filesize
        if (res == 0 && t != null && t.isRawValue()) {
            res = t.bsobAsLong();
        }

        return res;
    }

    @Override
    public int getSources() {
        Tag t = Tag.getTagById(Tag.FT_SOURCES, info);
        return (t != null)?t.asIntValue():0;
    }

    @Override
    public int getCompleteSources() {
        Tag t = Tag.getTagById(Tag.FT_COMPLETE_SOURCES, info);
        return (t != null)?t.asIntValue():0;
    }

    @Override
    public int getMediaBitrate() {
        Tag t = Tag.getTagById(Tag.FT_MEDIA_BITRATE, info);
        return (t != null)?t.asIntValue():0;
    }

    @Override
    public int getMediaLength() {
        Tag t = Tag.getTagById(Tag.FT_MEDIA_LENGTH, info);
        return (t != null)?t.asIntValue():0;
    }

    @Override
    public String getMediaCodec() {
        Tag t = Tag.getTagById(Tag.FT_MEDIA_CODEC, info);
        return (t != null)?t.asStringValue():"";
    }

    @Override
    public String getMediaAlbum() {
        Tag t = Tag.getTagById(Tag.FT_MEDIA_ALBUM, info);
        return (t != null)?t.asStringValue():"";
    }

}
