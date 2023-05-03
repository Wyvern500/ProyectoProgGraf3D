package com.lecheros.display.widgets.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.lecheros.animation.Animation;
import com.lecheros.animation.Animator;
import com.lecheros.animation.Keyframe;
import com.lecheros.animator.Engine;
import com.lecheros.display.handlers.AnimationStuffDisplayHandler;
import com.lecheros.display.widgets.IKeyboardListener;
import com.lecheros.display.widgets.IMouseListener;
import com.lecheros.display.widgets.Widget;
import com.lecheros.model.JgModel;
import com.lecheros.model.ModelPart;

public class AnimationTimeline extends Widget implements IMouseListener<AnimationTimeline>, 
					IKeyboardListener<AnimationTimeline> {

	List<Keyframe> keyframes;

	private float minX;
	private float maxVX;
	private float maxX;
	private float deltaX;
	private int animDur;
	private int selected;
	private float scale;

	private float scrollX;
	private float scrollWidth;
	private int offset;
	private float scrollY;
	
	private float oldProg;

	private int kfSize;
	
	private boolean visible;

	private Animator animator;

	private Animation animation;

	private AnimationStuffDisplayHandler animStuff;

	private AnimationTickMarker marker;
	
	public AnimationTimeline(float x, float y, float w, float h, AnimationStuffDisplayHandler stuff) {
		super(x, y, w, h);
		this.visible = true;
		this.minX = x;
		this.maxVX = x + w;
		this.deltaX = this.maxVX - this.minX;
		this.scale = 1;
		this.selected = -1;
		this.kfSize = 15;
		animator = null;
		animStuff = stuff;
		marker = new AnimationTickMarker(this, x, y, w, h, pos.x, pos.x + w);
		if (this.keyframes != null) {
			int xtemp = (int) (this.minX
					+ (MathUtils.lerp(0, this.deltaX,
							this.keyframes.get(this.keyframes.size() - 1).startVisualTick / this.animDur) * this.scale)
					- 10);
			this.maxX = xtemp;
			this.scrollY = y;// 220;
			this.scrollX = this.minX;
			this.scrollWidth = (int) MathUtils.lerp(0, this.deltaX, (this.maxVX - 10) / xtemp);
			float part1 = this.minX - this.scrollX;
			float part2 = ((this.deltaX) - this.scrollWidth);
			if (part1 != 0 && part2 != 0) {
				this.offset = (int) Math.abs(MathUtils.lerp(0, this.maxX - this.maxVX, part1 / part2));
			} else {
				this.offset = 0;
			}
		} else {
			this.maxX = this.maxVX;
			this.scrollY = y;// 340;
			this.scrollWidth = deltaX;
			this.scrollX = this.minX;
		}
	}

	@Override
	public void tick() {
		if (animator != null) {
			if(animator.getAnimation() != null) {
				animator.update();
				if(animator.play) {
					animStuff.getField().setValue(animator.getTick());
				} else {
					String text = animStuff.getField().getText();
					float val = 0;
					if(text.matches("[0-9.]+")) {
						val = Math.max(0, Float.parseFloat(text));
					} else {
						val = 0.0f;
					}
					if(val > animator.getAnimation().getDuration()) {
						val = animator.getAnimation().getDuration();
					}
					float prog = MathUtils.lerp(0, animator.getAnimation().getDuration(), 
							marker.getProgress()); 
					val = prog;
					if(prog != oldProg) {
						animator.setTick(val);
						animStuff.getField().setValue(val);
					}
					this.oldProg = prog;
				}
				marker.tick();
			}
		}
	}

	@Override
	public void render(ShapeRenderer sr, BitmapFont font, SpriteBatch batch) {
		if (animator == null)
			return;
		if (this.visible && animator.getAnimation() != null) {
			// Logger.getGlobal().info("keyframes != null? " + (keyframes != null)
			// + " animation != null? " + (animation != null));
			if (keyframes != null && animation != null) {
				sr.begin(ShapeType.Filled);
				/*for (int i = 0; i < keyframes.size(); i++) {
					Keyframe kf = keyframes.get(i);
					if (kf == null) {
						Logger.getGlobal().info("null at index: " + i);
					}
					int kfx = (int) (this.minX
							+ (MathUtils.lerp(0, this.deltaX, this.keyframes.get(i).startTick 
									/ (float) this.animDur)
									* this.scale)
							 - this.offset);
					Color color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
					boolean animSelected = animator.getCurrent() - 1 == i;
					if (this.selected == i) {
						color.set(0.6F, 0.6F, 0.6F, 1.0f);
					} else if (animator.getCurrent() == i) {// || animSelected)) {
						color.set(0.3F, 0.3F, 0.3F, 1.0f);
					} else if (animSelected) {
						color.set(1.0F, 1.0F, 1.0F, 1.0f);
					}
					sr.setColor(color);
					sr.rect(kfx, pos.y + ((h * 0.5f) - (7)), kfSize, kfSize);
					// }
				}*/
				renderProgressBar(sr);
				// renderScrollbar();
				float offset = w / animation.getDuration();
				for(int i = 0; i < animation.getDuration(); i++) {
					sr.rect(pos.x + (i * offset), pos.y + h - 10, 2, 10);
				}
				sr.end();
			}
			marker.render(sr, font, batch);
		}
	}

	public void renderProgressBar(ShapeRenderer sr) {
		float x = MathUtils.clamp(MathUtils.lerp(this.minX, this.maxX, animator.getTick() 
				/ animator.getAnimation().getDuration())
				- this.offset, minX, maxVX - 10);
		if (x >= minX - 10 && x < maxVX - 10) {
			sr.rect(x, pos.y, 2, h);
			// sr.rect(x, pos.y + h - 20, deltaX, 20);
		}
	}

	@Override
	public void mouseDragged(int x, int y) {
		marker.mouseDragged(x, y);
	}

	@Override
	public void mouseDown(int x, int y, int button) {
		marker.mouseDown(x, y, button);
		if (animator == null)
			return;
		if (this.visible && animator.getAnimation() != null) {
			if (keyframes != null && animation != null) {
				for (int i = 0; i < keyframes.size(); i++) {
					Keyframe kf = keyframes.get(i);
					if (kf == null) {
						Logger.getGlobal().info("null at index: " + i);
					}
					int kfx = (int) (this.minX
							+ (MathUtils.lerp(0, this.deltaX, this.keyframes.get(i).startTick 
									/ (float) this.animDur)
									* this.scale)
							 - this.offset);
					float kfy = pos.y + ((h * 0.5f) - (7));
					if(x > kfx && x < kfx + kfSize &&
							y > kfy && y < kfy + kfSize) {
						Logger.getGlobal().info("Keyframe with index " + i 
								+ " clicked at tick " + kf.startTick + " with duration " + 
								kf.dur);
						if(selected != i) {
							selected = i;
						} else {
							selected = -1;
						}
					}
				}
			}
		}
	}

	@Override
	public void mouseUp(int x, int y, int button) {
		marker.mouseUp(x, y, button);
	}

	@Override
	public void mouseScroll(float delta) {
		marker.mouseScroll(delta);
	}
	
	@Override
	public void mouseMoved(int x, int y) {
		
	}

	@Override
	public void onKeyDown(int keycode) {
		
	}

	@Override
	public void onKeyUp(int keycode) {
		
	}
	
	@Override
	public void dispose() {

	}

	public void clear() {
		if (keyframes != null) {
			keyframes.clear();
		}
		animation = null;
		animator = null;
	}

	public void setModel(JgModel model) {
		animator = model.getAnimator();
		animator.setModel(model);
	}

	public void setAnimation(Animation anim) {
		if (animator != null) {
			animator.setAnimation(anim);
			this.animation = anim;
			setupForAnimation(anim);
			this.setKeyframes(anim.getKeyframes(), anim.getDuration());
			//System.out.println("Animation keyframes size: " + anim.getKeyframes().size());
			Logger.getGlobal().info("Animation set succesfully");
		} else {
			Logger.getGlobal().info("Animator can't be null");
		}
	}
	
	private void setupForAnimation(Animation anim) {
		// Creando un mapa para almacenar las partes y sus transform entries correspondientes
		Map<ModelPart, Pair<SetupAnimationData, SetupAnimationData>> parts = new HashMap<>();
		// Obteniendo las partes que tienen keyframes
		// Iterando sobre los keyframes de la animacion
		anim.keyframesMap.values().forEach((k) -> {
			System.out.println("Foreach Keyframe");
			// Iterando sobre las traslaciones del keyframe actual
			k.translations.keySet().forEach((p) -> {
				if(!parts.containsKey(p)) {
					// Si no se tiene registrada la parte asignala, asi como tambien unos valores
					// por defecto
					List<Pair<Keyframe, float[]>> tr = new ArrayList<>();
					tr.add(new Pair<>(k, k.translations.get(p)));
					parts.put(p, new Pair<>(new SetupAnimationData(tr), 
							new SetupAnimationData(new ArrayList<>())));
				} else {
					// Si ya los tiene registrados simplemente agrega esa traslacion a la
					// lista de la parte correspondiente
					parts.get(p).getFirst().data.add(new Pair<>(k, k.translations.get(p)));
				}
			});
			// Lo mismo que hice con translations pero ahora con rotations
			k.rotations.keySet().forEach((p) -> {
				if(!parts.containsKey(p)) {
					List<Pair<Keyframe, float[]>> rt = new ArrayList<>();
					rt.add(new Pair<>(k, k.rotations.get(p)));
					parts.put(p, new Pair<>(new SetupAnimationData(new ArrayList<>()), 
							new SetupAnimationData(rt)));
				} else {
					parts.get(p).getSecond().data.add(new Pair<>(k, k.rotations.get(p)));
				}
			});
		});
		// Creando los Keyframes Entry
		List<KeyframeEntry> entries = new ArrayList<>();
		for(ModelPart part : parts.keySet()) {
			KeyframeEntry entry = new KeyframeEntry(part, animStuff.getEngine());
			entries.add(entry);
		}
		// Inicializando los transforms entry
		for(int i = 0; i < entries.size(); i++) {
			KeyframeEntry entry = entries.get(i);
			SetupAnimationData first = parts.get(entry.getModelPart()).getFirst();
			SetupAnimationData second = parts.get(entry.getModelPart()).getSecond();
			float ey = getAnimStuff().getEngine().getAnimStuff().getKeyframeManager()
					.getPos().y + (i * 60);
			// Translation
			for(Pair<Keyframe, float[]> data : first.data) {
				entry.addTransformEntry(data.getFirst(), getAnimStuff().getAnimTime(), 
						ey, data.getSecond(), true);
			}
			// Rotation
			for(Pair<Keyframe, float[]> data : second.data) {
				entry.addTransformEntry(data.getFirst(), getAnimStuff().getAnimTime(), 
						ey, data.getSecond(), false);
			}
		}
		for(KeyframeEntry entry : entries) {
			getAnimStuff().getEngine().getAnimStuff().getKeyframeManager()
				.addKeyframeEntry(entry);
			System.out.println("Entry transform entries size: " + entry.getTransformEntries()
			.size());
		}
		System.out.println("Entries size: " + entries.size());
	}
	
	public void addKeyframe(Keyframe kf) {
		animation.addKeyframe(kf);
		setKeyframes(animation.getKeyframes(), animation.getDuration());
	}

	public void setKeyframes(List<Keyframe> keyframes, int animDur) {
		this.animDur = animDur;
		this.keyframes = keyframes;
		if (this.keyframes != null) {
			if(keyframes.isEmpty()) return;
			float x = this.minX
					+ (MathUtils.lerp(0, this.deltaX,
							this.keyframes.get(this.keyframes.size() - 1).startVisualTick / this.animDur) * this.scale)
					- 10 - this.offset;
			this.maxX = (int) x;
			this.scrollWidth = (int) MathUtils.lerp(0, this.deltaX, (this.maxVX - 10) / x);
		}
	}

	public void onNewModel(JgModel model) {
		setModel(model);
	}

	// Getters and setters
	
	public AnimationStuffDisplayHandler getAnimStuff() {
		return animStuff;
	}
	
	public AnimationTickMarker getAnimationTickMarker() {
		return marker;
	}
	
	public void setAnimator(Animator animator) {
		this.animator = animator;
	}

	public int getSelected() {
		return selected;
	}
	
	public float getMaxVX() {
		return maxVX;
	}

	public Animator getAnimator() {
		return animator;
	}

	public Animation getAnimation() {
		return animation;
	}
	
	public class SetupAnimationData {
		
		private List<Pair<Keyframe, float[]>> data;
		
		public SetupAnimationData(List<Pair<Keyframe, float[]>> data) {
			this.data = data;
		}

		public List<Pair<Keyframe, float[]>> getData() {
			return data;
		}
		
	}
	
	public class Pair<T, K> {
		
		private T t;
		private K k;
		
		public Pair(T t, K k) {
			this.t = t;
			this.k = k;
		}
		
		public T getFirst() {
			return t;
		}
		
		public K getSecond() {
			return k;
		}
		
	}

}
