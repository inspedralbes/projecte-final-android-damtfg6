package com.example.recuerdate.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class CloseButton extends Actor {
    private Texture texture;

    public CloseButton() {
        texture = new Texture(Gdx.files.internal("botoTanca.png"));
        setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());

        // Agrega un listener de clic al botón
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY());
    }
}
