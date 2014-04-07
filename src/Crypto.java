class Crypto {
	static int[] h = new int[] { 1, 7, 10, 13, 19, 23, 28, 31, 32, 44, 49, 68,
			70, 79, 82, 86, 91, 94, 97, 100, 103, 109, 129, 130, 133, 139 };

	public static int[] fill1(char[] c) {
		int[] i = new int[c.length];
		for (int x = 0; x < c.length; x++) {
			i[x] = ((int) c[x]) - 65;
			i[x] = h[i[x]];
		}
		return i;
	}

	public static int[] fill(char[] c) {
		int[] i = new int[c.length];
		for (int x = 0; x < c.length; x++) {
			i[x] = (int) c[x];
		}
		return i;
	}

	public static char[] fill(String k) {
		char[] c = new char[k.length()];
		for (int x = 0; x < k.length(); x++) {
			c[x] = k.charAt(x);
		}
		return c;
	}
}
