package ru.hashtag.bglparser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dict extends ArrayList<DictEntry> {
    DictMetaInfo metaInfo;

    public static DictBuilder builder() {
        return new DictBuilder();
    }

    public static class DictBuilder {
        private DictMetaInfo metaInfo;
        private List<DictEntry> entries = new ArrayList<>();

        DictBuilder() {
        }

        public DictBuilder withEntries(List<DictEntry> c) {
            this.entries = c;
            return this;
        }

        public DictBuilder metaInfo(DictMetaInfo metaInfo) {
            this.metaInfo = metaInfo;
            return this;
        }

        public Dict build() {
            Dict dict = new Dict(metaInfo);
            dict.addAll(entries);
            return dict;
        }
    }
}
