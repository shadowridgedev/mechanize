package com.gistlabs.mechanize.json.nodeImpl;

import java.util.*;

import com.gistlabs.mechanize.json.JsonNode;
import com.gistlabs.mechanize.json.query.NodeHelper;


public class JsonNodeHelper implements NodeHelper<JsonNode> {

    public String getValue(JsonNode element) {
		return element.getContent();
	}
	
    public boolean hasAttribute(JsonNode element, String name) {
		return element.hasAttribute(name);
	}

    public Collection<JsonNode> getAttributes(JsonNode element) {
		Collection<JsonNode> result = new LinkedHashSet<JsonNode>();
		for(String key : element.getAttributes()) {
			result.add(element.getChild(key));
		}
		return result;
	}
    
    public Collection<? extends JsonNode> getDescendentNodes(JsonNode node) {
    	Collection<JsonNode> result = new LinkedHashSet<JsonNode>();
    	
    	LinkedList<JsonNode> toProcess = new LinkedList<JsonNode>();
    	toProcess.add(node);
    	while(!toProcess.isEmpty()) {
    		JsonNode first = toProcess.removeFirst();
    		List<JsonNode> children = first.getChildren();
    		result.addAll(children);
    		toProcess.addAll(children);
    	}
    	
    	return result;
	}
    
    public Collection<? extends JsonNode> getChildNodes(JsonNode node) {
		return node.getChildren();
	}

    public String getName(JsonNode n) {
		return n.getName();
	}
    
    public JsonNode getNextSibling(JsonNode node) {
    	throw new UnsupportedOperationException("Haven't implemented this yet");
    	//DOMHelper.getNextSiblingElement(node);
		// TODO Auto-generated method stub
	}

    @SuppressWarnings("unchecked")
	public Index getIndexInParent(JsonNode node, boolean byType) {
		String type = byType ? node.getName() : "*";
		
		List<JsonNode> children;
		JsonNode parent = node.getParent();
		if (parent==null)
			children = Collections.EMPTY_LIST;
		else
			children = parent.getChildren(type);

		return new Index(children.indexOf(node), children.size());
	}

	public JsonNode getRoot(JsonNode node) {
		JsonNode root = node;
		while (root.getParent()!=null)
			root = root.getParent();
		return root;
	}

}
