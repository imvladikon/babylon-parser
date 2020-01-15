package ru.hashtag.bglparser;

import lombok.SneakyThrows;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import java.util.SortedMap;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

public class ParserTest {

    @SneakyThrows
    @Test
    public void parse1() {
        final URI file = ParserTest.class.getResource("/eng_heb_idioms.bgl").toURI();
        Parser parser = new Parser(Charset.defaultCharset(), Charset.defaultCharset());
        Dict dict = parser.parse(new File(file));
        assertEquals(319, dict.size());
    }

    @SneakyThrows
    @Test
    public void parse2() {
        final URI file = ParserTest.class.getResource("/eng_heb_idioms.bgl").toURI();
        Parser parser = new Parser();
        Dict dict = parser.parse(new File(file));
        final List<DictEntry> entries = dict.stream().filter(dictEntry -> dictEntry.getHeadWord().contains("bolt from a blue")).collect(toList());
        assertEquals(1, entries.size());
        assertEquals("כרעם ביום בהיר<BR>" , entries.get(0).getDefinition());
    }

    @SneakyThrows
    @Test
    public void parse3() {
        final URI file = ParserTest.class.getResource("/eng_heb_idioms.bgl").toURI();
        Parser parser = new Parser();
        Dict dict = parser.parse(new File(file));
        for (DictEntry entry: dict) {
            System.out.println(entry.getHeadWord() + " = " + entry.getDefinition());
        }
    }

    @Test
    public void parse4() {
//        final SortedMap<String, Charset> map = Charset.availableCharsets();
    }
}