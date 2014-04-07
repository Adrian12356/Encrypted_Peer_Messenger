public class Decryption extends Crypto {
	String r;

	public Decryption(String m, String k) {

		char[] c = fill(k);
		int[] i = fill1(c);
		int[] s = split(m);
		int[] n = Unscramble(s, i);
		if (!k.equals(null)) {
			r = Reconstruct(n);
		}
	}

	public static int[] Unscramble(int[] m, int[] c) {
		int x = 0;
		int y = 0;
		while (x < m.length) {
			m[x] = m[x] / c[y];
			x++;
			y++;
			if (y >= c.length) {
				y = 0;
			}
		}
		return m;
	}

	public static String Reconstruct(int[] m) {
		String q = "";
		char[] w = new char[m.length];
		for (int x = 0; x < m.length; x++) {
			w[x] = (char) m[x];
			q = q + w[x];
		}
		return q;

	}

	public static int[] split(String m) {
		int count = 0;
		for (int s = 0; s < m.length(); s++) {
			if (m.charAt(s) == ':') {
				count++;
			}
		}
		String[] ms = new String[count];
		int y = 0;
		int x = 0;
		while (m.length() > 0) {
			if (m.charAt(x) == ':') {
				int g = m.indexOf(':');
				ms[y] = m.substring(0, g);
				m = m.substring(g + 1);
				y++;
			}
			x++;
			if (x >= m.length()) {
				x = 0;
			}
		}

		int[] mi = new int[ms.length];
		for (int t = 0; t < ms.length; t++) {
			mi[t] = Integer.parseInt(ms[t]);
		}
		return mi;
	}

}
