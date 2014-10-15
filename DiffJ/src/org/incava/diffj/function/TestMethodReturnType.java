package org.incava.diffj.function;

import static org.incava.diffj.function.Method.RETURN_TYPE_CHANGED;

import org.incava.analysis.FileDiffChange;
import org.incava.diffj.ItemsTest;
import org.incava.diffj.util.Lines;

public class TestMethodReturnType extends ItemsTest {
	public TestMethodReturnType(String name) {
		super(name);
	}

	public void testReturnTypeChanged() {
		evaluate(new Lines("class Test {", "    Object foo() { return null; }",
				"", "}"),

				new Lines("class Test {", "",
						"    Integer foo() { return null; }", "}"),

				new FileDiffChange(locrg(2, 5, 10), locrg(3, 5, 11),
						RETURN_TYPE_CHANGED, "Object", "Integer"));
	}
}
