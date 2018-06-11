package application;

public class ScalingManager {

	private MainController mainController;
	private double mediaW;
	private double mediaH;
	private double canvasW;
	private double canvasH;
	
	public ScalingManager(MainController mainController) {
		this.mainController = mainController;
	}
	
	public void setMediaDimension() {
		mediaW = mainController.getPlayer().getMedia().getWidth();
		mediaH =mainController.getPlayer().getMedia().getHeight();
	}
	
	public void setCanvasDimension() {
		canvasW = mainController.getCanvas().getWidth();
		canvasH = mainController.getCanvas().getHeight();	
	}
	
	public void normalizePoint(Point p) {
		double normX = p.getX()/canvasW;
		double normY = p.getY()/canvasH;
		p.setNormCord(normX, normY);
		p.setX((int)(mediaW * normX));
		p.setY((int)(mediaH * normY));
	}
	
	public void updatePointPos(Point p) {
		double x = p.getNormX() * canvasW;
		double y = p.getNormY() * canvasH;
		p.setDrawX((int)x);
		p.setDrawY((int)y);
	}
	
	public double[] calcNormalize(int x, int y) {
		double[] norm = new double[2];
		norm[0] = x/canvasW;
		norm[1] = y/canvasH;
		return norm;
	}
	
	
	
}
