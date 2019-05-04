package tool;

import java.io.*;

/**
 * Stemmer, implementing the Porter Stemming Algorithm
 *
 * The Stemmer class transforms a word into its root form. The input word can be
 * provided a character at time (by calling add()), or at once by calling one of
 * the various stem(something) methods.
 */

public class PorterStemmer {
	private char[] b;
	private int i, /* b中的元素位置（偏移量） */
			i_end, /* 要抽取词干单词的结束位置 */
			j, k;
	private static final int INC = 50;

	/* 随着b的大小增加数组要增长的长度（防止溢出） */
	public PorterStemmer() {
		b = new char[INC];
		i = 0;
		i_end = 0;
	}

	/**
	 * 增加一个字符到要存放待处理的单词的数组。添加完字符时， 可以调用stem(void)方法来进行抽取词干的工作。
	 */
	public void add(char ch) {
		if (i == b.length) {
			char[] new_b = new char[i + INC];
			for (int c = 0; c < i; c++)
				new_b[c] = b[c];
			b = new_b;
		}
		b[i++] = ch;
	}

	/*
	 * 增加wLen长度的字符数组到存放待处理的单词的数组b。
	 */
	public void add(char[] w, int wLen) {
		if (i + wLen >= b.length) {
			char[] new_b = new char[i + wLen + INC];
			for (int c = 0; c < i; c++)
				new_b[c] = b[c];
			b = new_b;
		}
		for (int c = 0; c < wLen; c++)
			b[i++] = w[c];
	}

	/**
	 * After a word has been stemmed, it can be retrieved by toString(), or a
	 * reference to the internal buffer can be retrieved by getResultBuffer and
	 * getResultLength (which is generally more efficient.)
	 */
	public String toString() {
		return new String(b, 0, i_end);
	}

	/**
	 * Returns the length of the word resulting from the stemming process.
	 */
	public int getResultLength() {
		return i_end;
	}

	/**
	 * Returns a reference to a character buffer containing the results of the
	 * stemming process. You also need to consult getResultLength() to determine the
	 * length of the result.
	 */
	public char[] getResultBuffer() {
		return b;
	}

	/**
	 * 当i为辅音时，返回真；否则为假。
	 * 
	 * @param i
	 * @return
	 */
	private final boolean cons(int i) {
		switch (b[i]) {
		case 'a':
		case 'e':
		case 'i':
		case 'o':
		case 'u':
			return false;
		case 'y':
			return (i == 0) ? true : !cons(i - 1);// y开头，为辅；否则看i-1位，如果i-1位为辅，y为元，反之亦然。
		default:
			return true;
		}
	}

	/*
	 * m() measures the number of consonant sequences between 0 and j. if c is a
	 * consonant sequence and v a vowel sequence, and <..> indicates arbitrary
	 * presence,
	 * 
	 * <c><v> gives 0 <c>vc<v> gives 1 <c>vcvc<v> gives 2 <c>vcvcvc<v> gives 3 ....
	 */
	// 计算在0和j之间辅音序列的个数。
	private final int m() {
		int n = 0; // 辅音序列的个数，初始化
		int i = 0; // 偏移量
		while (true) {
			if (i > j)
				return n; // 如果超出最大偏移量，直接返回n
			if (!cons(i))
				break; // 如果是元音，中断
			i++; // 辅音移一位，直到元音的位置
		}
		i++; // 移完辅音，从元音的第一个字符开始
		while (true)// 循环计算vc的个数
		{
			while (true) // 循环判断v
			{
				if (i > j)
					return n;
				if (cons(i))
					break; // 出现辅音则终止循环
				i++;
			}
			i++;
			n++;
			while (true) // 循环判断c
			{
				if (i > j)
					return n;
				if (!cons(i))
					break;
				i++;
			}
			i++;
		}
	}

	//vowelinstem() 为真 <=> 0,...j 包含一个元音
	private final boolean vowelinstem() {
		int i;
		for (i = 0; i <= j; i++)
			if (!cons(i))
				return true;
		return false;
	}

	//doublec(j) 为真 <=> j,(j-1) 包含两个一样的辅音
	private final boolean doublec(int j) {
		if (j < 1)
			return false;
		if (b[j] != b[j - 1])
			return false;
		return cons(j);
	}

	/* cvc(i) is 为真 <=> i-2,i-1,i 有形式： 辅音 - 元音 - 辅音
	   并且第二个c不是 w,x 或者 y. 这个用来处理以e结尾的短单词。 e.g.
	 
	   cav(e), lov(e), hop(e), crim(e), 但不是
	   snow, box, tray.
	 
	*/
	private final boolean cvc(int i) {
		if (i < 2 || !cons(i) || cons(i - 1) || !cons(i - 2))
			return false;
		{
			int ch = b[i];
			if (ch == 'w' || ch == 'x' || ch == 'y')
				return false;
		}
		return true;
	}

	private final boolean ends(String s) {
		int l = s.length();
		int o = k - l + 1;
		if (o < 0)
			return false;
		for (int i = 0; i < l; i++)
			if (b[o + i] != s.charAt(i))
				return false;
		j = k - l;
		return true;
	}

	// setto(s) 设置 (j+1),...k 到s字符串上的字符, 并且调整k值
	private final void setto(String s) {
		int l = s.length();
		int o = j + 1;
		for (int i = 0; i < l; i++)
			b[o + i] = s.charAt(i);
		k = j + l;
	}

	/* r(s) is used further down. */

	private final void r(String s) {
		if (m() > 0)
			setto(s);
	}

	/* step1() 处理复数，ed或者ing结束的单词。比如：
	 
    caresses  ->  caress
    ponies    ->  poni
    ties      ->  ti
    caress    ->  caress
    cats      ->  cat

    feed      ->  feed
    agreed    ->  agree
    disabled  ->  disable

    matting   ->  mat
    mating    ->  mate
    meeting   ->  meet
    milling   ->  mill
    messing   ->  mess

    meetings  ->  meet
*/
	private final void step1() {
		if (b[k] == 's') {
			if (ends("sses"))
				k -= 2; // 以“sses结尾”
			else if (ends("ies"))
				setto("i"); // 以ies结尾，置为i
			else if (b[k - 1] != 's')
				k--; // 两个s结尾不处理
		}
		if (ends("eed")) {
			if (m() > 0)
				k--;
		} // 以“eed”结尾，当m>0时，左移一位
		else if ((ends("ed") || ends("ing")) && vowelinstem()) {
			k = j;
			if (ends("at"))
				setto("ate");
			else if (ends("bl"))
				setto("ble");
			else if (ends("iz"))
				setto("ize");
			else if (doublec(k))// 如果有两个相同辅音
			{
				k--;
				{
					int ch = b[k];
					if (ch == 'l' || ch == 's' || ch == 'z')
						k++;
				}
			} else if (m() == 1 && cvc(k))
				setto("e");
		}
	}

	/* 第二步，如果单词中包含元音，并且以y结尾，将y改为i */
	private final void step2() {
		if (ends("y") && vowelinstem())
			b[k] = 'i';
	}

	/* step3() 将双后缀的单词映射为单后缀。 所以 -ization ( = -ize 加上
	   -ation) 被映射到 -ize 等等。 注意在去除后缀之前必须确保
	   m() > 0. 
	*/
	private final void step3() {
		if (k == 0)
			return;
		switch (b[k - 1]) {
		case 'a':
			if (ends("ational")) {
				r("ate");
				break;
			}
			if (ends("tional")) {
				r("tion");
				break;
			}
			break;
		case 'c':
			if (ends("enci")) {
				r("ence");
				break;
			}
			if (ends("anci")) {
				r("ance");
				break;
			}
			break;
		case 'e':
			if (ends("izer")) {
				r("ize");
				break;
			}
			break;
		case 'l':
			if (ends("bli")) {
				r("ble");
				break;
			}
			if (ends("alli")) {
				r("al");
				break;
			}
			if (ends("entli")) {
				r("ent");
				break;
			}
			if (ends("eli")) {
				r("e");
				break;
			}
			if (ends("ousli")) {
				r("ous");
				break;
			}
			break;
		case 'o':
			if (ends("ization")) {
				r("ize");
				break;
			}
			if (ends("ation")) {
				r("ate");
				break;
			}
			if (ends("ator")) {
				r("ate");
				break;
			}
			break;
		case 's':
			if (ends("alism")) {
				r("al");
				break;
			}
			if (ends("iveness")) {
				r("ive");
				break;
			}
			if (ends("fulness")) {
				r("ful");
				break;
			}
			if (ends("ousness")) {
				r("ous");
				break;
			}
			break;
		case 't':
			if (ends("aliti")) {
				r("al");
				break;
			}
			if (ends("iviti")) {
				r("ive");
				break;
			}
			if (ends("biliti")) {
				r("ble");
				break;
			}
			break;
		case 'g':
			if (ends("logi")) {
				r("log");
				break;
			}
		}
	}

	/*第四步，处理-ic-，-full，-ness等等后缀。和步骤3有着类似的处理 */
	private final void step4() {
		switch (b[k]) {
		case 'e':
			if (ends("icate")) {
				r("ic");
				break;
			}
			if (ends("ative")) {
				r("");
				break;
			}
			if (ends("alize")) {
				r("al");
				break;
			}
			break;
		case 'i':
			if (ends("iciti")) {
				r("ic");
				break;
			}
			break;
		case 'l':
			if (ends("ical")) {
				r("ic");
				break;
			}
			if (ends("ful")) {
				r("");
				break;
			}
			break;
		case 's':
			if (ends("ness")) {
				r("");
				break;
			}
			break;
		}
	}

	/* 第五步，在<c>vcvc<v>情形下，去除-ant，-ence等后缀。 */

	private final void step5() {
		if (k == 0)
			return;
		/* for Bug 1 */ switch (b[k - 1]) {
		case 'a':
			if (ends("al"))
				break;
			return;
		case 'c':
			if (ends("ance"))
				break;
			if (ends("ence"))
				break;
			return;
		case 'e':
			if (ends("er"))
				break;
			return;
		case 'i':
			if (ends("ic"))
				break;
			return;
		case 'l':
			if (ends("able"))
				break;
			if (ends("ible"))
				break;
			return;
		case 'n':
			if (ends("ant"))
				break;
			if (ends("ement"))
				break;
			if (ends("ment"))
				break;
			/* element etc. not stripped before the m */
			if (ends("ent"))
				break;
			return;
		case 'o':
			if (ends("ion") && j >= 0 && (b[j] == 's' || b[j] == 't'))
				break;
			/* j >= 0 fixes Bug 2 */
			if (ends("ou"))
				break;
			return;
		/* takes care of -ous */
		case 's':
			if (ends("ism"))
				break;
			return;
		case 't':
			if (ends("ate"))
				break;
			if (ends("iti"))
				break;
			return;
		case 'u':
			if (ends("ous"))
				break;
			return;
		case 'v':
			if (ends("ive"))
				break;
			return;
		case 'z':
			if (ends("ize"))
				break;
			return;
		default:
			return;
		}
		if (m() > 1)
			k = j;
	}

	/*第六步，在m()>1的情况下，移除末尾的“e” */
	private final void step6() {
		j = k;
		if (b[k] == 'e') {
			int a = m();
			if (a > 1 || a == 1 && !cvc(k - 1))
				k--;
		}
		if (b[k] == 'l' && doublec(k) && m() > 1)
			k--;
	}

	/** 通过调用add()方法来讲单词放入词干器数组b中
	  * 可以通过下面的方法得到结果：
	  * getResultLength()/getResultBuffer() or toString().
	  */
	public void stem() {
		k = i - 1;
		if (k > 1) {
			step1();
			step2();
			step3();
			step4();
			step5();
			step6();
		}
		i_end = k + 1;
		i = 0;
	}

	/**
	 * Test program for demonstrating the Stemmer. It reads text from a a list of
	 * files, stems each word, and writes the result to standard output. Note that
	 * the word stemmed is expected to be in lower case: forcing lower case must be
	 * done outside the Stemmer class. Usage: Stemmer file-name file-name ...
	 */
	public static void main(String[] args) {
		char[] w = new char[501];
		PorterStemmer s = new PorterStemmer();
		for (int i = 0; i < args.length; i++)
			try {
				FileInputStream in = new FileInputStream(new File("F:/Eclipse/eclipse/code/SearchEngine/WebPage/HandledEnglishPage/handled_Web_E_10.txt"));
				try {
					while (true)
					{
						int ch = in.read();
						if (Character.isLetter((char) ch)) {
							int j = 0;
							while (true) {
								ch = Character.toLowerCase((char) ch);
								w[j] = (char) ch;
								if (j < 500)
									j++;
								ch = in.read();
								if (!Character.isLetter((char) ch)) {
									/* to test add(char ch) */
									for (int c = 0; c < j; c++)
										s.add(w[c]);
									/* or, to test add(char[] w, int j) */
									/* s.add(w, j); */
									s.stem();
									{
										String u;
										/* and now, to test toString() : */
										u = s.toString();
										/* to test getResultBuffer(), getResultLength() : */
										/* u = new String(s.getResultBuffer(), 0, s.getResultLength()); */
										System.out.print(u);
									}
									break;
								}
							}
						}
						if (ch < 0)
							break;
						System.out.print((char) ch);
					}
				} catch (IOException e) {
					System.out.println("error reading " + args[i]);
					break;
				}
			} catch (FileNotFoundException e) {
				System.out.println("file " + args[i] + " not found");
				break;
			}
	}
}