package app;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SvgConverter {
	public static void main(String[] args) throws IOException {
		final var path = Paths.get("img");
		try (var s = Files.list(path)) {
			String css = s.filter(p -> p.getFileName().toString().toLowerCase().endsWith(".svg"))
					.map(p -> new SvgContent(p.getFileName().toString(), readFile(p)))
					.map(a -> new SvgContent(a.name.substring(0, a.name.length() - 4), a.content))//
					.peek(SvgContent::write)//
					.map(SvgContent::toCss)//
					.collect(Collectors.joining());
			Files.write(Paths.get("out.css"), css.getBytes(StandardCharsets.UTF_8));
		}
	}

	private static String readFile(Path path) {
		try {
			return Files.lines(path).collect(Collectors.joining());
		} catch (IOException e) {
			e.printStackTrace();
			throw new UncheckedIOException(e);
		}
	}

	private static class SvgContent {
		String name;
		String content;

		public SvgContent(String name, String content) {
			super();
			this.name = name;
			this.content = content;
		}

		private static final Pattern COMMENTS = Pattern.compile("<!--.*?-->");
		private static final Pattern WS = Pattern.compile("\s+");
		private static final List<Pattern> INK1s = Arrays.asList(//
				Pattern.compile("inkscape:\\S*?=\".*?\"") //
				, Pattern.compile("sodipodi:\\S*?=\".*?\"")//
				, Pattern.compile("<sodipodi:.*?/>")//
				, Pattern.compile("<metadata.*?</metadata>")//
				, Pattern.compile("viewBox=\"[ 0-9\\.]*\"")//
				, Pattern.compile("enable-background=\"[ a-z0-9\\.]*\"")//
				, Pattern.compile("id=\"[a-z0-9]*\"")//
				, Pattern.compile("<defs\s*/>")//
		);
		private static final List<String> HEADERS = Arrays.asList(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>",
				"xmlns:inkscape=\"http://www.inkscape.org/namespaces/inkscape\"",
				"xmlns:sodipodi=\"http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd\"",
				"xmlns:cc=\"http://creativecommons.org/ns#\"", "xmlns:cc=\"http://creativecommons.org/ns#\"", //
				"xmlns:dc=\"http://purl.org/dc/elements/1.1/\"", //
				"xmlns:svg=\"http://www.w3.org/2000/svg\"", //
				"xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"", //
				"id='图层_1'", //
				"xml:space=\"preserve\""//
				, "id=\"图层_1\""//
				, "version=\"1.1\""
				,"x=\"0px\"",
				"y=\"0px\"");
		private static final List<String> FIRSTS = Arrays.asList(//
				"<path\\s*d=\"[-,A-Za-z0-9.\\s]*?\"\\s/>" //
		);

		private String shortenContent() {
			System.out.print("Handling: " + name + " -> ");
			String[] out = { content };

			HEADERS.forEach(h -> {
				out[0] = out[0].replace(h, "");
			});
			out[0] = COMMENTS.matcher(out[0]).replaceAll("");
			INK1s.forEach(p -> {
				out[0] = p.matcher(out[0]).replaceAll("");
			});
			out[0] = WS.matcher(out[0]).replaceAll(" ");
			FIRSTS.forEach(p -> {
				System.out.print(out[0].length() + ";");
				out[0] = out[0].replaceFirst(p, "");
			});

			System.out.println(out[0].length());
			return out[0];
		}

		private String wrap() {
			return "background-image: url(data:image/svg+xml;base64,"
					+ Base64.getEncoder().encodeToString(shortenContent().getBytes(StandardCharsets.UTF_8)) + ");";
			// return "background-image: url(\"data:image/svg+xml," +
			// encode(shortenContent()) + "\");";
		}

		private static String encode(String string) {
			return string.replace("<", s1).replace(">", s2).replace('"', '\'');
		}

		private static final String s1 = urlEncode("<");
		private static final String s2 = urlEncode(">");

		private static String urlEncode(String string) {
			return URLEncoder.encode(string, StandardCharsets.UTF_8);
		}

		String toCss() {
			return ".mr-tile-face[mr-tile=\"" + firstUpper(name) + "\"]{\n  " + wrap() + "\n}\n";
		}

		private String firstUpper(String string) {
			return Character.toUpperCase(string.charAt(0)) + string.substring(1);
		}

		void write() {
			try {
				Files.write(Paths.get("out", name + ".svg"), shortenContent().getBytes(StandardCharsets.UTF_8));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
