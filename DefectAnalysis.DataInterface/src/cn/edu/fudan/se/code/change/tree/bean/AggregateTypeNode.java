/**
 * 
 */
package cn.edu.fudan.se.code.change.tree.bean;

import java.util.ArrayList;

/**
 * @author Lotay
 *
 */
public class AggregateTypeNode {
	private String changeType = "NORMAL";
	private String postNodeValue = null;
	private String preNodeValue = null;
	private AggregateTypeNode parentNode = null;
	private ArrayList<AggregateTypeNode> children = new ArrayList<AggregateTypeNode>();
	private CodeTreeNode codeTreeNode;

	public AggregateTypeNode(CodeTreeNode codeTreeNode) {
		super();
		this.codeTreeNode = codeTreeNode;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getPostNodeValue() {
		return postNodeValue;
	}

	public void setPostNodeValue(String postNodeValue) {
		this.postNodeValue = postNodeValue;
	}

	public String getPreNodeValue() {
		return preNodeValue;
	}

	public void setPreNodeValue(String preNodeValue) {
		this.preNodeValue = preNodeValue;
	}

	public AggregateTypeNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(AggregateTypeNode parentNode) {
		this.parentNode = parentNode;
	}

	public ArrayList<AggregateTypeNode> getChildren() {
		return children;
	}

	public void addChildNode(AggregateTypeNode child) {
		if (child != null) {
			this.children.add(child);
			child.setParentNode(this);
		}
	}

	public CodeTreeNode getCodeTreeNode() {
		return codeTreeNode;
	}

	public void setCodeTreeNode(CodeTreeNode codeTreeNode) {
		this.codeTreeNode = codeTreeNode;
	}

	@Override
	public String toString() {
		return "AggregateTypeNode [changeType=" + changeType
				+ ", postNodeValue=" + postNodeValue + ", preNodeValue="
				+ preNodeValue + "]";
	}
}
