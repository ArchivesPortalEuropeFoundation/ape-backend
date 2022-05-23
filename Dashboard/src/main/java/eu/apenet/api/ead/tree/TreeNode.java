package eu.apenet.api.ead.tree;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class TreeNode {
	private static final String LAZY = "\"isLazy\": true";
	private static final String FOLDER = "\"isFolder\": true";
	private static final String EXPANDED = "\"expand\": true";
	private static final String SELECTED = "\"select\": true";
	private static final String CHILDREN = "\"children\" :";
	public static final String END_ARRAY = "]";
	public static final String START_ARRAY = "[";
	protected static final String END_ITEM = "}";
	protected static final String START_ITEM = "{";
	public static final String COMMA = ",";
	private boolean icon = false;
	private String cssClass;
	private String title;
	private boolean selected;
	private Boolean hideCheckbox;
	private String key;
	private boolean folder = false;
	private boolean expanded = false;
	private boolean lazy = true;
	private String more;
	private Integer start;
	private List<? extends TreeNode> children = new ArrayList<TreeNode>();

	public boolean isIcon() {
		return icon;
	}
	public void setIcon(boolean icon) {
		this.icon = icon;
	}
	public String getCssClass() {
		return cssClass;
	}
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public Boolean getHideCheckbox() {
		return hideCheckbox;
	}
	public void setHideCheckbox(Boolean hideCheckbox) {
		this.hideCheckbox = hideCheckbox;
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public boolean isFolder() {
		return folder;
	}
	public void setFolder(boolean folder) {
		this.folder = folder;
	}
	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	public boolean isLazy() {
		return lazy;
	}
	public void setLazy(boolean lazy) {
		this.lazy = lazy;
	}
	public List<? extends TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<? extends TreeNode> children) {
		this.children = children;
	}
	
	public String getMore() {
		return more;
	}
	public void setMore(String more) {
		this.more = more;
	}
	
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	protected String toStringInternal(){
		return "";
	}
	@Override
	public final String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(START_ITEM);
		if (!icon){
			builder.append("\"icon\":");
			builder.append(" false");
			builder.append(COMMA);
		}
		if (StringUtils.isNotBlank(cssClass)){
			builder.append("\"addClass\":");
			builder.append(" \"" + cssClass + "\"");
			builder.append(COMMA);
		}
		if (StringUtils.isNotBlank(key)){
			builder.append("\"key\":");
			builder.append(" \"" + key + "\"");
			builder.append(COMMA);
		}
		if (selected){
			builder.append(SELECTED);
			builder.append(COMMA);
		}
		if (StringUtils.isNotBlank(more)){
			builder.append("\"more\":");
			builder.append(" \"" + more + "\"");	
			builder.append(COMMA);
		}
		if (start != null){
			builder.append("\"start\":");
			builder.append(" \"" + start + "\"");	
			builder.append(COMMA);
		}
		if (hideCheckbox != null){
			builder.append("\"hideCheckbox\":");
			builder.append(hideCheckbox);
			builder.append(COMMA);
		}
		String toStringInternal = toStringInternal();
		if (StringUtils.isNotBlank(toStringInternal)){
			builder.append(toStringInternal);
			builder.append(COMMA);
		}
		builder.append("\"title\":\"");
		builder.append(title);
		builder.append("\"");
		if (folder){
			builder.append(COMMA);
			builder.append(FOLDER);
			builder.append(COMMA);
			if (expanded){
				builder.append(EXPANDED);
				builder.append(COMMA);
				addChildren(builder);
			}else if (lazy){
				builder.append(LAZY);
			}else {
				addChildren(builder);
			}
		}
		builder.append(END_ITEM);
		return builder.toString();
	}
	private void addChildren(StringBuilder builder){
		builder.append(CHILDREN);
		builder.append(START_ARRAY);
		boolean first = true;
		for (TreeNode child: children){
			if (first){
				first = false;
			}else {
				builder.append(COMMA);
			}
			builder.append(child.toString());
		}
		builder.append(END_ARRAY);
	}

}
