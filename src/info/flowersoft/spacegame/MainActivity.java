package info.flowersoft.spacegame;

import info.flowersoft.gameframe.AppActivity;
import info.flowersoft.gameframe.AppRenderer;
import android.os.Bundle;

public class MainActivity extends AppActivity {

	@Override
	public AppRenderer getRenderer(Bundle savedInstanceState) {
		return new MyApp(savedInstanceState, this);
	}

}
