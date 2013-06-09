package elements;
import java.text.*;
import com.sun.opengl.util.*;
import com.sun.opengl.util.j2d.*;
import java.awt.*;
import processing.core.PApplet;
//import javax.media.opengl.*;
import processing.opengl.PGraphicsOpenGL;

//import com.sun.opengl.util.awt.*;

public class VTextRenderer
{
	
	 /// ____________________________________________________
	 /// Members
	 int _w, _h;
	 PApplet p;
	 String _fontName;
	 int _fontSize;
	 TextRenderer _textRender;
	 Font font;
	
 public VTextRenderer(PApplet parent, String fontName, int size )
 {
	p = parent;
   _fontName = fontName;
   _fontSize = size;
   //Font _font = new Font(fontName, Font.TRUETYPE_FONT, size);
   //_textRender = new TextRenderer(null, false, false, null, false);
  // _textRender = new TextRenderer(new Font(fontName, Font.TRUETYPE_FONT, size));
   _textRender = new TextRenderer( new Font(fontName, Font.TRUETYPE_FONT, size), true, true, null, true );
   _textRender.setColor( (float)1.0f,(float) 1.0,(float) 1.0,(float) 1.0 );
   //_textRender.setUseVertexArrays( true );
 }

 public VTextRenderer(PApplet parent, String fontName, int size, boolean antialiased, boolean mipmap )
 {
	p = parent;
   _fontName = fontName;
   _fontSize = size;
   _textRender = new TextRenderer( new Font(fontName, Font.TRUETYPE_FONT, size), antialiased, true, null, mipmap );
   _textRender.setColor( (float)1.0f,(float) 1.0,(float) 1.0,(float) 1.0 );
   
   //_textRender.setUseVertexArrays( true );
 }
 
 
 public void print( String str, int x, int y )
 {
	 ((PGraphicsOpenGL)p.g).beginGL();
   _textRender.beginRendering( p.width, p.height, true );
   _textRender.draw( str, x, p.height-y );
   _textRender.endRendering();  
   _textRender.flush();
   ((PGraphicsOpenGL)p.g).endGL();
 }

 public void print( String str, float x, float y, float z )
 {
   print( str, x, y, z, 1.0f );
 }

 public void print( String str, float x, float y, float z, float s )
 {
	//p.pushMatrix();
	 p.scale( 1, -1, 1 );
	 ((PGraphicsOpenGL)p.g).beginGL();
	  
	 _textRender.begin3DRendering();
   //_textRender.beginRendering( p.width, p.height);
   _textRender.draw3D( str, x, y, z, s );
   _textRender.end3DRendering();  
   _textRender.flush();
   ((PGraphicsOpenGL)p.g).endGL();
  // p.popMatrix();
 }

 public void setColor( float c )
 {
   setColor( c, c, c, 1 );
 }

 public void setColor( float c, float a )
 {
   setColor( c, c, c, a );
 }

 public void setColor( float r, float g, float b )
 {
   setColor( r, g, b, 1 );
 }
 
 public void setColor( float r, float g, float b, float a )
 {
   _textRender.setColor( r, g, b, a );
 }
 
 public void setSmoothing( boolean flag )
 {
   _textRender.setSmoothing( flag );
 }



 
}
