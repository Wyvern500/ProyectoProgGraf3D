package com.lecheros.display.widgets;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.lecheros.model.ModelPart;

public class JgTree {

	private List<JgTreeNode> nodes;
	private Vector2 pos;
	private JgTreeNode selectedNode;
	private int spacing;
	private OnClickOnNode onClick;

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
		for (JgTreeNode node : nodes) {
			node.tick();
		}
	}

	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch sp) {
		renderShapes(sr);
		renderText(font, sp);
	}

	private void renderShapes(ShapeRenderer sr) {
		sr.begin(ShapeType.Filled);
		sr.rect(pos.x, pos.y, 4, 4);
		sr.end();
		for (JgTreeNode node : nodes) {
			node.renderShapes(sr);
		}
	}

	private void renderText(BitmapFont font, SpriteBatch sp) {
		for (JgTreeNode node : nodes) {
			node.renderText(font, sp);
		}
	}

	public void loopOverAllChilds() {

	}

	public List<JgTreeNode> getAllNodes() {
		// Listas necesarias para ejecutar el algoritmo
		List<JgTreeNode> nodes = new ArrayList<>();
		// A current le asigno los primeros hijos, que serian los hijos de tree
		List<JgTreeNode> current = this.nodes;
		List<JgTreeNode> queue = new ArrayList<>();
		boolean keepRunning = true;
		// Iniciamos un do while para que por lo menos se ejecute una vez, esto en
		// realidad
		// no se si tiene que ser asi pero por si acaso asi me funciono
		do {
			// Iteramos sobre current, que contiene los hijos actuales a checar
			for (JgTreeNode node : current) {
				// Si el nodo actual tiene hijos, meterlos a la lista para checar si estos
				// tambien tienen y asi iniciar un ciclo
				if (!node.childs.isEmpty()) {
					queue.add(node);
				}
				// Meto el nodo actual a la lista para contar los nodos netos, es decir,
				// cuantos hay en total
				nodes.add(node);
			}
			// Este if es necesario para saber cuando terminar el ciclo while
			if (!queue.isEmpty()) {
				// Obtenemos los hijos a checar que estan en la lista de espera
				current = queue.get(0).childs;
				// Removemos los hijos que acabamos de asignar para seguir con los proximos
				queue.remove(0);
			} else {
				// Si la lista de espera esta vacia entonces terminamos el while
				keepRunning = false;
			}
		} while (keepRunning);
		return nodes;
	}

	public void mouseDown(int x, int y) {
		for (JgTreeNode node : nodes) {
			node.mouseDown(x, y);
		}
	}

	public void clear() {
		nodes.clear();
	}

	public Vector2 getPos() {
		return pos;
	}

	public void setPos(float x, float y) {
		pos.set(x, y);
	}

	public void setOnClick(OnClickOnNode onClick) {
		this.onClick = onClick;
	}

	public OnClickOnNode getOnClick() {
		return onClick;
	}

	public JgTreeNode getSelectedNode() {
		return selectedNode;
	}

	public static interface OnClickOnNode {
		public void run(JgTreeNode node);
	}

	public static class JgTreeNode {

		private JgTree root;
		private JgTreeNode parent;

		private ModelPart part;

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

		public JgTreeNode(ModelPart part) {
			this(part.getName(), part);
		}

		public JgTreeNode(String text) {
			this(text, 100, 200, null);
		}

		public JgTreeNode(String text, ModelPart part) {
			this(text, 100, 20, part);
		}

		public JgTreeNode(String text, float w, float h, ModelPart part) {
			this.pos = new Vector2();
			this.w = w;
			this.h = h;
			this.text = text;
			this.visible = true;
			this.part = part;
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

		public void tick() {

		}

		public void renderShapes(ShapeRenderer sr) {
			if (visible) {
				Color c = sr.getColor();
				sr.begin(ShapeType.Filled);
				sr.setColor(Color.WHITE);
				sr.rect(pos.x, pos.y, w, h);
				if (!childs.isEmpty()) {
					sr.setColor(Color.BLACK);
					sr.rect(pos.x - 18, pos.y, 18, h);
					sr.setColor(Color.WHITE);
					sr.rect(pos.x - 16, pos.y + 2, 14, h - 4);
					if (!expanded) {
						sr.setColor(Color.GREEN);
					} else {
						sr.setColor(Color.RED);
					}
					sr.rect(pos.x - 14, pos.y + 4, 10, h - 8);
				}
				sr.setColor(Color.DARK_GRAY);
				if (root.selectedNode != null) { // 0d0d0d
					if (root.selectedNode == this) {
						sr.setColor(new Color(0x0d0d0dff));
					}
				}
				sr.rect(pos.x + 1, pos.y + 1, w - 1, h - 1);
				sr.end();
				sr.setColor(c);
				if (expanded) {
					for (JgTreeNode child : childs) {
						child.renderShapes(sr);
					}
				}
			}
		}

		public void renderText(BitmapFont font, SpriteBatch sp) {
			if (visible) {
				sp.begin();
				font.draw(sp, text, pos.x, pos.y + 2);
				sp.end();
				if (expanded) {
					for (JgTreeNode child : childs) {
						child.renderText(font, sp);
					}
				}
			}
		}

		public void updateChilds(Modify mod) {
			mod.modify(this);
			for (JgTreeNode child : childs) {
				child.updateChilds(mod);
			}
		}

		public void updateChilds(int dif) {
			if (!modified) {
				pos.y += this.dif;
				for (JgTreeNode child : childs) {
					child.dif = this.dif;
					child.modified = false;
					child.updateChilds(dif);
				}
				modified = true;
			}
		}

		public void mouseDown(int x, int y) {
			boolean canBeActive = true;
			if (parent != null) {
				canBeActive = parent.expanded;
			} else {
				canBeActive = true;
			}
			if (canBeActive) {
				if (pos.x < x && pos.x + w > x && pos.y < y && pos.y + h > y) {
					// Node clicked
					root.selectedNode = this;
					if (root.getOnClick() != null) {
						root.getOnClick().run(this);
					}

				} else if (pos.x - 18 < x && pos.x > x && pos.y < y && pos.y + h > y) {
					// Expand the node
					setExpanded(!expanded);
				}
				for (JgTreeNode child : childs) {
					child.mouseDown(x, y);
				}
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
			if (parent == null) {
				for (JgTreeNode node : root.nodes) {
					node.reset();
				}
				for (JgTreeNode node : root.nodes) {
					if (node.index > index) {
						node.dif = getChildsOffset() * (expanded ? 1 : -1);
						node.updateChilds((int) (childs.size() * h));
					}
				}
			} else {
				for (JgTreeNode node : parent.childs) {
					if (node.index > index) {
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
				if (!childsOfChilds.isEmpty()) {
					nodeChilds = childsOfChilds.get(0);
				}
				if (!childsOfChilds.isEmpty()) {
					childsOfChilds.remove(0);
				}
				for (JgTreeNode child : nodeChilds) {
					if (child.isExpanded()) {
						offsetY += h;
						childsOfChilds.add(child.childs);
					} else {
						offsetY += h;
					}
				}
			} while (!childsOfChilds.isEmpty());
			return offsetY;
		}

		public void sendDifToParent(int dif) {
			if (parent != null) {
				parent.sendDifToParent(dif);
			} else {
				this.dif = dif;
				for (JgTreeNode node : root.nodes) {
					if (node.index > index) {
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

		public ModelPart getModelPart() {
			return part;
		}

	}

	public interface Modify {
		public void modify(JgTreeNode node);
	}

}
