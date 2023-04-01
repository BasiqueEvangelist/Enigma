package cuchaz.enigma.gui.util;

import com.google.common.collect.Iterables;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;

public class SortedMutableTreeNode implements MutableTreeNode {
	protected final Comparator<TreeNode> comparator;
	protected MutableTreeNode parent;
	private final List<TreeNode> children;
	private boolean isSorted = true;

	public SortedMutableTreeNode(Comparator<TreeNode> comparator) {
		this.comparator = comparator;
		children = new ArrayList<>();
	}

	@Override
	public void insert(MutableTreeNode child, int index) {
		if (child == null) throw new IllegalArgumentException("child is null");
		if (index != 0) throw new IllegalArgumentException("index must always be 0");

		MutableTreeNode oldParent = (MutableTreeNode) child.getParent();

		if (oldParent != null) oldParent.remove(child);
		child.setParent(this);
		children.add(child);
		isSorted = false;
	}

	private void checkSorted() {
		if (!isSorted) {
			isSorted = true;
			children.sort(comparator);
		}
	}

	@Override
	public void remove(int index) {
		checkSorted();
		remove((MutableTreeNode) getChildAt(index));
	}

	@Override
	public void remove(MutableTreeNode node) {
		children.remove(node);
		node.setParent(null);
	}

	public Object getUserObject() {
		return null;
	}

	@Override
	public void setUserObject(Object object) {
		throw new IllegalStateException("no");
	}

	@Override
	public void removeFromParent() {
		parent.remove(this);
	}

	@Override
	public void setParent(MutableTreeNode newParent) {
		parent = newParent;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		checkSorted();
		return children.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public int getIndex(TreeNode node) {
		return Iterables.indexOf(children, other -> comparator.compare(node, other) == 0);
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public boolean isLeaf() {
		return children.isEmpty();
	}

	@Override
	public Enumeration<? extends TreeNode> children() {
		var iter = children.iterator();

		return new Enumeration<>() {
			@Override
			public boolean hasMoreElements() {
				return iter.hasNext();
			}

			@Override
			public TreeNode nextElement() {
				return iter.next();
			}
		};
	}

	public TreeNode[] getPath() {
		return getPathToRoot(this, 0);
	}

	protected TreeNode[] getPathToRoot(TreeNode aNode, int depth) {
		TreeNode[]              retNodes;

        /* Check for null, in case someone passed in a null node, or
           they passed in an element that isn't rooted at root. */
		if(aNode == null) {
			if(depth == 0)
				return null;
			else
				retNodes = new TreeNode[depth];
		}
		else {
			depth++;
			retNodes = getPathToRoot(aNode.getParent(), depth);
			retNodes[retNodes.length - depth] = aNode;
		}
		return retNodes;
	}
}
