package com.gistlabs.mechanize.json.nodeImpl;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gistlabs.mechanize.json.Node;
import com.gistlabs.mechanize.json.exceptions.JsonArrayException;
import com.gistlabs.mechanize.json.exceptions.JsonException;

public class ObjectNodeImpl extends AbstractNode {

	private final JSONObject obj;
	private Map<String,Node> children = new HashMap<String, Node>();

	public ObjectNodeImpl(JSONObject obj) {
		this("root", obj);
		
	}
	
	public ObjectNodeImpl(String name, JSONObject obj) {
		this(null, name, obj);
	}

	public ObjectNodeImpl(Node parent, String name, JSONObject obj) {
		super(parent, name);
		this.obj = obj;
	}

	@Override
	public String getAttribute(String key) {
		try {
			Object value = obj.get(key);
			if (value==JSONObject.NULL)
				return null;
			
			return value.toString();
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}

	@Override
	public void setAttribute(final String key, final String value) {
		try {
			obj.put(key, value==null ? JSONObject.NULL : value);
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}

	@Override
	public boolean hasAttribute(String key) {
		return obj.has(key);
	}

	@Override
	public Collection<String> getAttributes() {
		try {
			List<String> result = new ArrayList<String>();
			@SuppressWarnings("unchecked")
			Iterator<String> keys = this.obj.keys();
			while(keys.hasNext()) {
				String key = keys.next();
				if (isPrimitive(this.obj.get(key)))
					result.add(key);
			}
			return result;
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}

	@Override
	public String getContent() {
		return null;
	}

	@Override
	public void setContent(final String value) {
		throw new JsonException("JSON Objects can't directly have cpntent");
	}

	@Override
	public Node getChild(final String key) {
		if (!children.containsKey(key))
			children.put(key, getElementByType(key));
		
		return children.get(key);
	}
	
	protected boolean isPrimitive(Object jsonObject) {
		return !(jsonObject instanceof JSONObject || jsonObject instanceof JSONArray);
	}
	
	protected Node getElementByType(String key) {
		try {
			Object obj = this.obj.get(key);
			return factory(key, obj);
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}

	@Override
	public List<Node> getChildren() {
		try {
			List<Node> result = new ArrayList<Node>();
			@SuppressWarnings("unchecked")
			Iterator<String> keys = this.obj.keys();
			while(keys.hasNext()) {
				String key = keys.next();
				if (!isPrimitive(this.obj.get(key)))
					result.addAll(getChildren(key));
			}
			return result;
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}

	@Override
	public List<Node> getChildren(String key) {
		try {
			ArrayList<Node> result = new ArrayList<Node>();
			try {
				result.add(getChild(key));
			} catch(JsonArrayException e) {
				if (e.getArray()==null)
					throw e;
				
				JSONArray array = e.getArray();
				for(int i=0;i < array.length();i++){
					Object obj = array.get(i);
					result.add(factory(key, obj, array, i));
				}
			}
			return result;
		} catch (JSONException e) {
			throw new JsonException(e);
		}
	}

}