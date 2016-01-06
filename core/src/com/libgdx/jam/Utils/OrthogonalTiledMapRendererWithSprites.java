package com.libgdx.jam.Utils;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class OrthogonalTiledMapRendererWithSprites  extends OrthogonalTiledMapRenderer{
	float unitScale = 1;
	
	public OrthogonalTiledMapRendererWithSprites(TiledMap map, Batch batch) {
		super(map, batch);
	}

	//Constructor that takes into account the Box2D scale (MPP) to render the sprites at the same size as the BOx2D bodies
	public OrthogonalTiledMapRendererWithSprites (TiledMap map, float unitScale, Batch batch) {
		super(map, unitScale, batch);
		
		this.unitScale = unitScale;
	}

	//Proceduraly draw the sprites we'll add to an object layer in our tiled map
    @Override
    public void renderObject(MapObject object) {
        if(object instanceof TextureMapObject) {
            TextureMapObject textureObj = (TextureMapObject) object;
            
            batch.draw(textureObj.getTextureRegion(), 
            			textureObj.getX(), 
            			textureObj.getY(), 
            			textureObj.getTextureRegion().getRegionWidth() * unitScale, 
            			textureObj.getTextureRegion().getRegionHeight() * unitScale);
        }
    }
}
