package sz.gadgets;

import sz.util.*;

import java.awt.Color;
import java.util.*;


import crl.ui.graphicsUI.CharKey;
import crl.ui.graphicsUI.SwingSystemInterface;


public class MenuBox {
	
	private Vector items;
	private String title = "";

	//State Attributes
	private int currentPage;
	private int pages;
	
	//Components
	private int xpos, ypos, width, itemsPerPage;;
	private SwingSystemInterface si;
	public MenuBox(SwingSystemInterface g){
		this.si = g;
	}
	
	public void setPosition(int x, int y){
		xpos = x;
		ypos = y;
	}
	
	
	public void setWidth(int width){
		this.width = width;
	}
	
	public void setItemsPerPage(int ipp){
		itemsPerPage = ipp;
	}
	public void setMenuItems(Vector items){
		this.items = items;
	}

	private static Color TRANSPARENT_BLUE = new Color(0,0,0,250);
	private static Color COLOR_BORDER_IN = new Color(160,160,160);
	private static Color COLOR_BORDER_OUT = new Color(80,80,255);
	
	private int gap = 24;
	
	public void setGap(int val){
		gap = val;
	}
	
	
	
	public void draw(){
		int realW = width * 10 +20;
		int realH = (itemsPerPage+1)*gap+20;
		int realPosX = xpos*10 - 20;
		int realPosY = ypos*24 - 30;
		
		si.getGraphics2D().setColor(TRANSPARENT_BLUE);
		si.getGraphics2D().fillRect(realPosX+6, realPosY+6, realW-14, realH-14);
		si.getGraphics2D().setColor(COLOR_BORDER_OUT);
		si.getGraphics2D().drawRect(realPosX+6,realPosY+6,realW-14,realH-14);
		si.getGraphics2D().setColor(COLOR_BORDER_IN);
		si.getGraphics2D().drawRect(realPosX+8,realPosY+8,realW-18,realH-18);
		
		//pages = (int)(Math.floor((items.size()-1) / inHeight) +1);
		pages = (int)(Math.floor((items.size()-1) / (itemsPerPage)) +1);
		/*System.out.println("items.size() "+items.size());
		System.out.println("inHeight "+inHeight);*/
		si.print(xpos, ypos, title, Color.BLUE);
		Vector shownItems = Util.page(items, itemsPerPage, currentPage);
		
		int i = 0;
		for (; i < shownItems.size(); i++){
			
			GFXMenuItem item = (GFXMenuItem) shownItems.elementAt(i);
			si.printAtPixel(xpos*10, (ypos+1)*24+i*gap, ((char) (97 + i))+"." , Color.BLUE);
			if (item.getMenuImage() != null)
				si.drawImage((xpos+2)*10+5, ypos*24+ i * gap + (int)(gap * 0.3D), item.getMenuImage());
			String description = item.getMenuDescription();
			if (description.length() > width-2){
				description = description.substring(0,width-4);
			}
			String detail = item.getMenuDetail();
			if (detail != null && detail.length() > width-2){
				detail = detail.substring(0,width-4);
			}
			si.printAtPixel((xpos+6)*10, (ypos+1)*24 + i*gap, description, Color.WHITE);
			if (detail != null && !detail.equals("")){
				si.printAtPixel((xpos+6)*10, (ypos+1)*24 + i*gap+18, detail, Color.WHITE);
			}
		}
		//si.print(inPosition.x, inPosition.y, inHeight+" "+pageElements+" "+pages);
		/*for (; i < inHeight-promptSize; i++){
			si.print(inPosition.x, inPosition.y+i+promptSize+1, spaces);
		}*/
		si.refresh();
	}

	public void setBounds(int x, int y, int width, int height){
		this.xpos = x;
		this.ypos = y;
		this.width = width;
		this.itemsPerPage = height;
	}
	
	public Object getSelection (){
		int pageElements = itemsPerPage;
		while (true){
			
			draw();
			Vector shownItems = Util.page(items, pageElements, currentPage);
			CharKey key = new CharKey(CharKey.NONE);
			while (key.code != CharKey.SPACE &&
				   key.code != CharKey.UARROW &&
				   key.code != CharKey.DARROW &&
				   key.code != CharKey.N8 &&
				   key.code != CharKey.N2 &&
				   (key.code < CharKey.A || key.code > CharKey.A + pageElements-1) &&
				   (key.code < CharKey.a || key.code > CharKey.a + pageElements-1)
				   )
			   key = si.inkey();
			if (key.code == CharKey.SPACE)
				return null;
			if (key.code == CharKey.UARROW || key.code == CharKey.N8)
				if (currentPage > 0)
					currentPage --;
			if (key.code == CharKey.DARROW || key.code == CharKey.N2)
				if (currentPage < pages-1)
					currentPage ++;
			
			if (key.code >= CharKey.A && key.code <= CharKey.A + shownItems.size()-1)
				return shownItems.elementAt(key.code - CharKey.A);
			else
			if (key.code >= CharKey.a && key.code <= CharKey.a + shownItems.size()-1)
				return shownItems.elementAt(key.code - CharKey.a);
			si.restore();

		}
	}
	
	public void setTitle(String s){
		title = s;
	}
}