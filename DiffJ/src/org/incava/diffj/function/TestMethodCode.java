package org.incava.diffj.function;

import static org.incava.diffj.code.Code.CODE_ADDED;
import static org.incava.diffj.code.Code.CODE_CHANGED;
import static org.incava.diffj.code.Code.CODE_REMOVED;

import org.incava.diffj.ItemsTest;
import org.incava.diffj.util.Lines;

public class TestMethodCode extends ItemsTest {
	public TestMethodCode(String name) {
		super(name);
	}

	public void testCodeNotChanged() {
		evaluate(new Lines("class Test {", "    int bar() { return -1; }", "",
				"}"),

		new Lines("class Test {", "", "    int bar() { ", "        return -1;",
				"    }", "", "}"),

		NO_CHANGES);
	}

	public void testCodeChanged() {
		evaluate(
				new Lines("class Test {", "    int bar() { return -1; }", "",
						"}"),

				new Lines("class Test {", "", "    int bar() { ",
						"        return -2;", "    }", "}"),

				makeCodeChangedRef(CODE_CHANGED, "bar()", locrg(2, 25, 25),
						locrg(4, 17, 17)));
	}

	public void testCodeInserted() {
		evaluate(
				new Lines("class Test {", "    int bar() { return -1; }", "",
						"}"),

				new Lines("class Test {", "", "    int bar() { ",
						"        int i = 0;", "        return -1;", "    }",
						"}"),

				makeCodeAddedRef(CODE_ADDED, "bar()", locrg(2, 17, 22),
						locrg(4, 9, 18)));
	}

	public void testCodeDeleted() {
		evaluate(new Lines("class Test {", "    int bar() { ",
				"        int i = 0;", "        return -1;", "    }", "", "}"),

				new Lines("class Test {", "", "    int bar() { return -1; }",
						"}"),

				// makeCodeDeletedRef(CODE_REMOVED, "bar()", locrg(3, 9, 18),
				// locrg(3, 17, 22)));
				makeCodeDeletedRef(CODE_REMOVED, "bar()", locrg(3, 9, 18),
						locrg(3, 17, 22)));
	}

	public void testCodeInsertedAndChanged() {
		evaluate(
				new Lines("class Test {", "    int bar() { return -1; }", "",
						"}"),

				new Lines("class Test {", "", "    int bar() { ",
						"        int i = 0;", "        return -2;", "    }",
						"}"),

				makeCodeChangedRef(CODE_CHANGED, "bar()", locrg(2, 17, 25),
						locrg(4, 9, 5, 17)));
	}

	// See comment in TestDiff.java, with regard to misleading LCSes.

	public void misleading_diffs_testZipDiff() {
		evaluate(
				new Lines(
						"/**",
						" * This class implements an output stream filter for writing files in the",
						" * ZIP file format. Includes support for both compressed and uncompressed",
						" * entries.",
						" *",
						" * @author	David Connelly",
						" * @version	1.27, 02/07/03",
						" */",
						"    public",
						"    class ZipOutputStream extends DeflaterOutputStream implements ZipConstants {",
						"",
						"        /**",
						"         * Closes the current ZIP entry and positions the stream for writing",
						"         * the next entry.",
						"         * @exception ZipException if a ZIP format error has occurred",
						"         * @exception IOException if an I/O error has occurred",
						"         */",
						"        public void closeEntry() throws IOException {",
						"            ZipEntry e = entry;",
						"            if (e != null) {",
						"                switch (e.method) {",
						"                    case DEFLATED:",
						"                        if ((e.flag & 8) == 0) {",
						"                            // verify size, compressed size, and crc-32 settings",
						"                            if (e.size != def.getTotalIn()) {",
						"                                throw new ZipException(",
						"                                    \"invalid entry size (expected \" + e.size +",
						"                                    \" but got \" + def.getTotalIn() + \" bytes)\");",
						"                            }",
						"                            if (e.csize != def.getTotalOut()) {",
						"                                throw new ZipException(",
						"                                    \"invalid entry compressed size (expected \" +",
						"                                    e.csize + \" but got \" + def.getTotalOut() +",
						"                                    \" bytes)\");",
						"                            }",
						"                            if (e.crc != crc.getValue()) {",
						"                                throw new ZipException(",
						"                                    \"invalid entry CRC-32 (expected 0x\" +",
						"                                    Long.toHexString(e.crc) + \" but got 0x\" +",
						"                                    Long.toHexString(crc.getValue()) + \")\");",
						"                            }",
						"                        } else {",
						"                            e.size = def.getTotalIn();",
						"                            e.csize = def.getTotalOut();",
						"                            e.crc = crc.getValue();",
						"                            writeEXT(e);",
						"                        }",
						"                        def.reset();",
						"                        written += e.csize;",
						"                        break;", "                }",
						"            }", "        }", "", "    }"),

				new Lines(
						"    /**",
						"     * This class implements an output stream filter for writing files in the",
						"     * ZIP file format. Includes support for both compressed and uncompressed",
						"     * entries.",
						"     *",
						"     * @author	David Connelly",
						"     * @version	1.31, 12/19/03",
						"     */",
						"    public",
						"        class ZipOutputStream extends DeflaterOutputStream implements ZipConstants {",
						"",
						"            /**",
						"             * Closes the current ZIP entry and positions the stream for writing",
						"             * the next entry.",
						"             * @exception ZipException if a ZIP format error has occurred",
						"             * @exception IOException if an I/O error has occurred",
						"             */",
						"            public void closeEntry() throws IOException {",
						"                ZipEntry e = entry;",
						"                if (e != null) {",
						"                    switch (e.method) {",
						"                        case DEFLATED:",
						"                            if ((e.flag & 8) == 0) {",
						"                                // verify size, compressed size, and crc-32 settings",
						"                                if (e.size != def.getBytesRead()) {",
						"                                    throw new ZipException(",
						"                                        \"invalid entry size (expected \" + e.size +",
						"                                        \" but got \" + def.getBytesRead() + \" bytes)\");",
						"                                }",
						"                                if (e.csize != def.getBytesWritten()) {",
						"                                    throw new ZipException(",
						"                                        \"invalid entry compressed size (expected \" +",
						"                                        e.csize + \" but got \" + def.getBytesWritten() + \" bytes)\");",
						"                                }",
						"                                if (e.crc != crc.getValue()) {",
						"                                    throw new ZipException(",
						"                                        \"invalid entry CRC-32 (expected 0x\" +",
						"                                        Long.toHexString(e.crc) + \" but got 0x\" +",
						"                                        Long.toHexString(crc.getValue()) + \")\");",
						"                                }",
						"                            } else {",
						"                                e.size  = def.getBytesRead();",
						"                                e.csize = def.getBytesWritten();",
						"                                e.crc = crc.getValue();",
						"                                writeEXT(e);",
						"                            }",
						"                            def.reset();",
						"                            written += e.csize;",
						"                            break;",
						"                    }", "                }",
						"            }", "        }"),

				makeCodeChangedRef(CODE_CHANGED, "bar()", locrg(2, 17, 25),
						locrg(4, 9, 5, 17)));
	}
}
