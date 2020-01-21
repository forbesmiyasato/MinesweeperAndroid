package edu.pacificu.cs.minesweeperandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity{

    private MineSweeper_View_AndroidUI mMineSweeperView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMineSweeperView = new MineSweeper_View_AndroidUI(this);
        mMineSweeperView.setDifficulty(0);
        setContentView(mMineSweeperView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.easy:
                mMineSweeperView.setDifficulty(0);
                Log.d ("onOptionsItemSelected", "easy");
                mMineSweeperView.invalidate();
                return true;
            case R.id.medium:
                mMineSweeperView.setDifficulty(1);
                Log.d ("onOptionsItemSelected", "medium");
                mMineSweeperView.invalidate();
                return true;
            case R.id.hard:
                mMineSweeperView.setDifficulty(2);
                Log.d ("onOptionsItemSelected", "hard");
                mMineSweeperView.invalidate();
                return true;
            case R.id.about:
                Intent intent = new Intent(MainActivity.this, AboutScreen.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected (item);
        }
    }

}
