package ru.hashtag.bglparser;

import lombok.ToString;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

@ToString
public class DictMetaInfo {
    public String name;
    public String author;
    public Charset defaultCharset;
    public String srcLng;//TODO:locale
    public Charset srcEnc;
    public String dstLng;
    public Charset dstEnc;

    public DictMetaInfo(InputStream s, List<BglBlock> entryBlocks) throws IOException {
        StringBuilder headword;
        int type = -1;
        while (true) {
            headword = new StringBuilder();
            BglBlock block = BglBlock.read(s);
            if (block == null) {
                break;
            }
            if (block.type == 0 && block.data[0] == 8) {
                type = block.data[1];
                if (type > 64)
                    type -= 65;
                this.defaultCharset = CHARSETS_BGL[type];
            } else if (block.type == 1 || block.type == 10) {
                entryBlocks.add(block);
            } else if (block.type == 3) {
                int pos = 2;
                switch (block.data[1]) {
                    case 1:
                        for (int a = 0; a < block.length - 2; a++)
                            headword.append((char) block.data[pos++]);
                        this.name = headword.toString();
                        break;
                    case 2:
                        for (int a = 0; a < block.length - 2; a++)
                            headword.append((char) block.data[pos++]);
                        this.author = headword.toString();
                        break;
                    case 7:
                        this.srcLng = LANGUAGES_BGL[block.data[5]];
                        break;
                    case 8:
                        this.dstLng = LANGUAGES_BGL[block.data[5]];
                        break;
                    case 26:
                        type = block.data[2];
                        if (type > 64)
                            type -= 65;
                        this.srcEnc = CHARSETS_BGL[type];
                        break;
                    case 27:
                        type = block.data[2];
                        if (type > 64)
                            type -= 65;
                        this.dstEnc = CHARSETS_BGL[type];
                        break;
                }
            } else
                continue;
        }
    }

    static final String[] LANGUAGES_BGL = {	"English",
            "French",
            "Italian",
            "Spanish",
            "Dutch",
            "Portuguese",
            "German",
            "Russian",
            "Japanese",
            "Traditional Chinese",
            "Simplified Chinese",
            "Greek",
            "Korean",
            "Turkish",
            "Hebrew",
            "Arabic",
            "Thai",
            "Other",
            "Other Simplified Chinese dialects",
            "Other Traditional Chinese dialects",
            "Other Eastern-European languages",
            "Other Western-European languages",
            "Other Russian languages",
            "Other Japanese languages",
            "Other Baltic languages",
            "Other Greek languages",
            "Other Korean dialects",
            "Other Turkish dialects",
            "Other Thai dialects",
            "Polish",
            "Hungarian",
            "Czech",
            "Lithuanian",
            "Latvian",
            "Catalan",
            "Croatian",
            "Serbian",
            "Slovak",
            "Albanian",
            "Urdu",
            "Slovenian",
            "Estonian",
            "Bulgarian",
            "Danish",
            "Finnish",
            "Icelandic",
            "Norwegian",
            "Romanian",
            "Swedish",
            "Ukrainian",
            "Belarusian",
            "Farsi",
            "Basque",
            "Macedonian",
            "Afrikaans",
            "Faeroese",
            "Latin",
            "Esperanto",
            "Tamazight",
            "Armenian" };

    static final Charset[] CHARSETS_BGL = {	Charset.defaultCharset(), /* Default */
            Charset.forName("Windows-1252"), /* Latin */
            Charset.forName("ISO-8859-2"), /* Eastern European */
            Charset.forName("windows-1251"), /* Cyriilic */
            Charset.forName("Windows-932"), /* Japanese */
            Charset.forName("Windows-950"), /* Traditional Chinese */
            Charset.forName("GBK"), /* Simplified Chinese */
            Charset.forName("Windows-1257"), /* Baltic */
            Charset.forName("Windows-1253"), /* Greek */
            Charset.forName("Windows-949"), /* Korean */
            Charset.forName("windows-1254"), /* Turkish */
            Charset.forName("Windows-1255"), /* Hebrew */
            Charset.forName("Windows-1256"), /* Arabic */
            Charset.forName("Windows-874") /* Thai */
    };

}
