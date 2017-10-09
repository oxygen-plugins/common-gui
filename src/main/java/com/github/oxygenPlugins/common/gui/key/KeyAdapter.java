package com.github.oxygenPlugins.common.gui.key;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;

import org.dom4j.swing.LeafTreeNode;

public class KeyAdapter implements KeyListener {
	
	public final int KEY_PRESSED = 0;
	public final int KEY_TYPED = 1;
	public final int KEY_RELEASED = 2;
	protected Component component;
	
	public KeyAdapter(Component component){
		this(component, false);
	}
	
	public KeyAdapter(Component component, boolean listenToTab) {
		component.addKeyListener(this);
		component.setFocusTraversalKeysEnabled(!listenToTab);
		this.component = component;
	}
	
	@Override
	public void keyPressed(KeyEvent ke) {
		log(ke, KEY_PRESSED);
		int kecode = ke.getKeyCode();
		switch (kecode) {
		case KeyEvent.VK_ENTER:
			enterPressed(ke);
			break;
		case KeyEvent.VK_ESCAPE:
			escapePressed(ke);
			break;
		case KeyEvent.VK_DOWN:
			downPressed(ke);
			break;
		case KeyEvent.VK_UP:
			upPressed(ke);
			break;
		case KeyEvent.VK_RIGHT:
			rightPressed(ke);
			break;
		case KeyEvent.VK_LEFT:
			leftPressed(ke);
			break;
		case KeyEvent.VK_TAB:
			tabPressed(ke);
			break;
		case KeyEvent.VK_DELETE:
			deletePressed(ke);
			break;
		case KeyEvent.VK_BACK_SPACE:
			backspacePressed(ke);
			break;
		case KeyEvent.VK_CONTROL:
			ctrlPressed(ke);
			break;
		case KeyEvent.VK_SHIFT:
			shiftPressed(ke);
			break;
		case KeyEvent.VK_ALT:
			altPressed(ke);
			break;
		case KeyEvent.VK_SPACE:
			spacePressed(ke);
			break;
		default:
			break;
		}
	}
	@Override
	public void keyReleased(KeyEvent ke) {
		log(ke, KEY_RELEASED);
		int kecode = ke.getKeyCode();
		switch (kecode) {
		case KeyEvent.VK_ENTER:
			enterRelease(ke);
			break;
		case KeyEvent.VK_ESCAPE:
			escapeRelease(ke);
			break;
		case KeyEvent.VK_DOWN:
			downRelease(ke);
			break;
		case KeyEvent.VK_UP:
			upRelease(ke);
			break;
		case KeyEvent.VK_RIGHT:
			rightRelease(ke);
			break;
		case KeyEvent.VK_LEFT:
			leftRelease(ke);
			break;
		case KeyEvent.VK_TAB:
			tabRelease(ke);
			break;
		case KeyEvent.VK_DELETE:
			deleteRelease(ke);
			break;
		case KeyEvent.VK_BACK_SPACE:
			backspaceRelease(ke);
			break;
		case KeyEvent.VK_CONTROL:
			ctrlRelease(ke);
			break;
		case KeyEvent.VK_SHIFT:
			shiftRelease(ke);
			break;
		case KeyEvent.VK_ALT:
			altRelease(ke);
			break;
		case KeyEvent.VK_SPACE:
			spaceRelease(ke);
			break;
		default:
			break;
		}
	}
	@Override
	public void keyTyped(KeyEvent ke) {
		log(ke, KEY_TYPED);
		char kechar = ke.getKeyChar();
		switch (kechar) {
		case KeyEvent.VK_ENTER:
			enterTyped(ke);
			break;
		case KeyEvent.VK_ESCAPE:
			escapeTyped(ke);
			break;
		case KeyEvent.VK_TAB:
			tabTyped(ke);
			break;
		case KeyEvent.VK_DELETE:
			deleteTyped(ke);
			break;
		case KeyEvent.VK_BACK_SPACE:
			backspaceTyped(ke);
			break;
		case KeyEvent.VK_SPACE:
			spaceTyped(ke);
			break;
		default:
			break;
		}
	}
	
	public void log(KeyEvent ke, int status){}
	
	public void enterRelease(KeyEvent ke){}
	public void enterPressed(KeyEvent ke){}
	public void enterTyped(KeyEvent ke){}
	public void escapeRelease(KeyEvent ke){}
	public void escapePressed(KeyEvent ke){}
	public void escapeTyped(KeyEvent ke){}
	public void downRelease(KeyEvent ke){}
	public void downPressed(KeyEvent ke){}
	public void upRelease(KeyEvent ke){}
	public void upPressed(KeyEvent ke){}
	public void rightRelease(KeyEvent ke){}
	public void rightPressed(KeyEvent ke){}
	public void leftRelease(KeyEvent ke){}
	public void leftPressed(KeyEvent ke){}

	public void tabRelease(KeyEvent ke){}
	public void tabPressed(KeyEvent ke){}
	public void tabTyped(KeyEvent ke){}
	public void deleteRelease(KeyEvent ke){}
	public void deletePressed(KeyEvent ke){}
	public void deleteTyped(KeyEvent ke){}
	public void backspaceRelease(KeyEvent ke){}
	public void backspacePressed(KeyEvent ke){}
	public void backspaceTyped(KeyEvent ke){}
	public void ctrlRelease(KeyEvent ke){}
	public void ctrlPressed(KeyEvent ke){}
	public void shiftRelease(KeyEvent ke){}
	public void shiftPressed(KeyEvent ke){}
	public void altRelease(KeyEvent ke){}
	public void altPressed(KeyEvent ke){}

	public void spaceRelease(KeyEvent ke){}
	public void spaceTyped(KeyEvent ke) {}
	public void spacePressed(KeyEvent ke){}

}
