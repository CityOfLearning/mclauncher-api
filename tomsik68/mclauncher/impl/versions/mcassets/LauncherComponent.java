package sk.tomsik68.mclauncher.impl.versions.mcassets;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@Deprecated
final class LauncherComponent extends Applet implements AppletStub {
	private static final long serialVersionUID = -6942044817024235085L;
	private final ClassLoader loader;
	private final JSplitPane splitPane;
	private final DefaultTableModel model;
	protected boolean active = false;
	private Applet minecraft;
	private HashMap<String, Object> params = new HashMap<String, Object>();

	public LauncherComponent(URLClassLoader loader) {
		System.setProperty("minecraft.applet.WrapperClass", this.getClass().getName());
		this.loader = loader;
		params.put("fullscreen", "false");
		setLayout(new BorderLayout());
		splitPane = new JSplitPane();
		splitPane.setEnabled(false);
		splitPane.setResizeWeight(1.0D);
		splitPane.setOrientation(0);
		model = new DefaultTableModel();
		model.addColumn("Key");
		model.addColumn("Value");
		model.setRowCount(5);
		JTable tbParameters = new JTable(model);
		splitPane.setLeftComponent(tbParameters);

		JButton btStart = new JButton("Start Minecraft");
		btStart.addActionListener(arg0 -> LauncherComponent.this.startMinecraft());
		splitPane.setRightComponent(btStart);
	}

	@Override
	public void appletResize(int width, int height) {
		this.setSize(width, height);
		minecraft.setSize(width, height);
	}

	public void createApplet() {
		try {
			minecraft = ((Applet) loader.loadClass("net.minecraft.client.MinecraftApplet").newInstance());
			minecraft.setStub(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public URL getDocumentBase() {
		try {
			return new URL("http://www.minecraft.net/game/");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getParameter(String name) {
		if (params.containsKey(name)) {
			return params.get(name).toString();
		}
		try {
			String superValue = super.getParameter(name);
			if (superValue != null) {
				model.addRow(new Object[] { name, superValue });
				return superValue;
			}
		} catch (Exception e) {
			params.put(name, "");

			model.addRow(new Object[] { name, "" });
		}
		return null;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	public void replace(Applet applet) {
		minecraft = applet;
		applet.setStub(this);
		applet.setSize(getWidth(), getHeight());

		setLayout(new BorderLayout());
		this.add(applet, "Center");

		applet.init();
		active = true;
		applet.start();
		validate();
	}

	public void setAll(Map<String, Object> params2) {
		params.putAll(params2);
	}

	public void setParameter(String k, String v) {
		params.put(k, v);
		model.addRow(new Object[] { k, v });
	}

	@Override
	public void start() {
		this.add(splitPane);
	}

	public void startMinecraft() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				while (active) {
					LauncherComponent.this.repaint();
					try {
						Thread.sleep(100L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
		for (int i = 0; i < model.getRowCount(); i++) {
			if ((model.getValueAt(i, 0) != null) && (model.getValueAt(i, 1) != null)) {
				params.put(model.getValueAt(i, 0).toString(), model.getValueAt(i, 1).toString());
			}
		}
		if (minecraft == null) {
			createApplet();
		}
		replace(minecraft);
	}

	@Override
	public void stop() {
		active = false;
	}

	@Override
	public void update(Graphics g) {
		if (minecraft != null) {
			minecraft.paint(g);
		}
	}
}