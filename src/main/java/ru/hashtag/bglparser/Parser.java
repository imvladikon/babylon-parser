package ru.hashtag.bglparser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Parser {
	Charset srcEncoding;
	Charset dstEncoding;

	public Dict parse(File file) throws IOException {
		String unzippedFilename = File.createTempFile(file.getName(), ".tmp").getAbsolutePath();
		File unzippedFile = new File(unzippedFilename);
		Files.deleteIfExists(Path.of(unzippedFilename));
		unzipBglFile(file, unzippedFile);
		return parseUnzippedFile(unzippedFile);
	}

	private Dict parseUnzippedFile(File unzippedFile) throws IOException {
		try (FileInputStream unzippedStream = new FileInputStream(unzippedFile)) {
			List<BglBlock> entryBlocks = new ArrayList<>();
			final DictMetaInfo metaInfo = new DictMetaInfo(unzippedStream, entryBlocks);
			return Dict.builder()
					.metaInfo(metaInfo)
					.withEntries(entryBlocks.stream()
							.map(b -> new DictEntry(b,
													Optional.ofNullable(srcEncoding)
															.orElse(metaInfo.srcEnc),
													Optional.ofNullable(dstEncoding)
															.orElse(metaInfo.dstEnc)))
							.collect(Collectors.toList()))
					.build();
		}
	}

	private void unzipBglFile(File bglFile, File dstFile) throws IOException {
		try (FileInputStream in = new FileInputStream(bglFile)) {
			byte[] buf = new byte[6];
			int pos = in.read(buf);
			// First four bytes: BGL signature 0x12340001 or 0x12340002
			// (big-endian)
			if (pos < 6 || (buf[0] == 0x12 && buf[1] == 0x34 && buf[2] == 0x00
				&& (buf[4] == 0x01 || buf[4] == 0x02))) {
				in.close();
				throw new IOException("Invalid file: no BGL signature: " + bglFile);
			}
			int gzipHeaderPos = (buf[4] & 0xFF) << 8 | (buf[5] & 0xFF);
			if (gzipHeaderPos < 6) {
				in.close();
				throw new IOException("No gzip ptr");
			}
			in.skip(gzipHeaderPos - 6);
			try (GZIPInputStream gzip = new GZIPInputStream(in);
					PrintStream out = new PrintStream(dstFile)) {
				byte[] gzipbuf = new byte[512];
				int gziplen;
				while ((gziplen = gzip.read(gzipbuf)) != -1) {
					out.write(gzipbuf, 0, gziplen);
				}
			}
		}
	}
}
