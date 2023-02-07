package com.lecheros.display.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class JgTree {

	private List<JgTreeNode> nodes;
	private Vector2 pos;
	private JgTreeNode selectedNode;
	private int spacing;
	
	public JgTree() {
		nodes = new ArrayList<>();
		pos = new Vector2();
		spacing = 40;
	}
	
	public void add(JgTreeNode node) {
		node.root = this;
		node.level = 0;
		node.spacing = spacing;
		node.pos.x = pos.x;
		node.pos.y = pos.y + (nodes.size() * node.h);
		node.index = nodes.size();
		nodes.add(node);
	}
	
	public void tick() {
		
	}
	
	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch sp) {
		renderShapes(sr);
		renderText(font, sp);
	}
	
	private void renderShapes(ShapeRenderer sr) {
		sr.begin(ShapeType.Filled);
		sr.rect(pos.x, pos.y, 4, 4);
		sr.end();
		for(JgTreeNode node : nodes) {
			node.renderShapes(sr);
		}
	}
	
	private void renderText(BitmapFont font, SpriteBatch sp) {
		for(JgTreeNode node : nodes) {
			node.renderText(font, sp);
		}
	}
	
	public void mouseDown(int x, int y) {
		for(JgTreeNode node : nodes) {
			node.mouseDown(x, y);
		}
	}
	
	public void setPos(float x, float y) {
		pos.set(x, y);
	}
	
	public JgTreeNode getSelectedNode() {
		return selectedNode;
	}
	
	public static class JgTreeNode {
		
		private JgTree root;
		private JgTreeNode parent;
		
		private int index;
		private int level;
		private int spacing;
		private int dif;
		
		private boolean expanded;
		private boolean visible;
		private boolean modified;
		
		private float w;
		private float h;
		
		private String text;
		
		private Vector2 pos;
		
		private List<JgTreeNode> childs;
		
		public JgTreeNode(String text) {
			this(text, 100, 20);
		}
		
		public JgTreeNode(String text, float w, float h) {
			this.pos = new Vector2();
			this.w = w;
			this.h = h;
			this.text = text;
			this.childs = new ArrayList<>();
		}
		
		public void add(JgTreeNode node) {
			node.level = level + 1;
			node.pos.x = pos.x + spacing;
			node.parent = this;
			node.spacing = spacing;
			node.index = childs.size();
			node.root = root;
			childs.add(node);
			node.pos.y = pos.y + (childs.size() * node.h);
		}
		
		public void renderShapes(ShapeRenderer sr) {
			if(visible) {
				Color c = sr.getColor();
				sr.begin(ShapeType.Filled);
				sr.setColor(Color.WHITE);
				sr.rect(pos.x, pos.y, w, h);
				if(!childs.isEmpty()) {
					sr.setColor(Color.BLACK);
					sr.rect(pos.x - 18, pos.y, 18, h);
					sr.setColor(Color.WHITE);
					sr.rect(pos.x - 16, pos.y+2, 14, h-4);
					if(!expanded) {
						sr.setColor(Color.GREEN);
					} else {
						sr.setColor(Color.RED);
					}
					sr.rect(pos.x - 14, pos.y+4, 10, h-8);
				}
				sr.setColor(Color.DARK_GRAY);
				sr.rect(pos.x + 1, pos.y + 1, w - 1, h - 1);
				sr.end();
				sr.setColor(c);
				if(expanded) {
					for(JgTreeNode child : childs) {
						child.renderShapes(sr);
					}
				}
			}
		}
		
		public void renderText(BitmapFont font, SpriteBatch sp) {
			if(visible) {
				sp.begin();
				font.draw(sp, text, pos.x, pos.y+2);
				sp.end();
				if(expanded) {
					for(JgTreeNode child : childs) {
						child.renderText(font, sp);
					}
				}
			}
		}
		
		public void updateChilds(int dif) {
			if(!modified) {
				pos.y += this.dif;
				for(JgTreeNode child : childs) {
					child.dif = this.dif;
					child.modified = false;
					child.updateChilds(dif);
				}
				modified = true;
			}
		}
		
		public void mouseDown(int x, int y) {
			if(pos.x < x && pos.x + w > x &&
					pos.y < y && pos.y + h > y) {
				// Node clicked
				Logger.getGlobal().info("Node clicked: " + text);
				root.selectedNode = this;
			} else if(pos.x - 18 < x && pos.x > x && pos.y < y && pos.y + h > y) {
				// Expand the node
				setExpanded(!expanded);
			}
			for(JgTreeNode child : childs) {
				child.mouseDown(x, y);
			}
		}
		
		public void reset() {
			this.modified = false;
		}

		public boolean isExpanded() {
			return expanded;
		}

		public void setExpanded(boolean expanded) {
			this.expanded = expanded;
			if(parent == null) {
				for(JgTreeNode node : root.nodes) {
					node.reset();
				}
				for(JgTreeNode node : root.nodes) {
					if(node.index > index) {
						node.dif = getChildsOffset() * (expanded ? 1 : -1);
						node.updateChilds((int)(childs.size()*h));
					}
				}
			} else {
				for(JgTreeNode node : parent.childs) {
					if(node.index > index) {
						int dif = getChildsOffset();
						dif *= expanded ? 1 : -1;
						node.reset();
						node.dif = dif;
						node.updateChilds(dif);
					}
				}
				sendDifToParent(getChildsOffset() * (expanded ? 1 : -1));
			}
		}
		
		// Loop over all the childs to get the resulting y offset
		public int getChildsOffset() {
			int offsetY = 0;
			List<JgTreeNode> nodeChilds = childs;
			List<List<JgTreeNode>> childsOfChilds = new ArrayList<>();
			do {
				if(!childsOfChilds.isEmpty()) {
					nodeChilds = childsOfChilds.get(0);
				}
				if(!childsOfChilds.isEmpty()) {
					childsOfChilds.remove(0);
				}
				for(JgTreeNode child : nodeChilds) {
					if(child.isExpanded()) {
						offsetY += h;
						childsOfChilds.add(child.childs);
					} else {
						offsetY += h;
					}
				}
			} while(!childsOfChilds.isEmpty());
			return offsetY;
		}
		
		public void sendDifToParent(int dif) {
			if(parent != null) {
				parent.sendDifToParent(dif);
			} else {
				this.dif = dif;
				for(JgTreeNode node : root.nodes) {
					if(node.index > index) {
						node.reset();
						node.dif = this.dif;
						node.updateChilds(this.dif);
					}
				}
			}
		}

		public boolean isVisible() {
			return visible;
		}

		public void setVisible(boolean visible) {
			this.visible = visible;
		}
		
	}
	
}
