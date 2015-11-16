package uy.edu.ucu.matchapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import org.parceler.Parcels;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uy.edu.ucu.matchapp.models.LeagueTable;
import uy.edu.ucu.matchapp.models.SoccerSeason;
import uy.edu.ucu.matchapp.models.StandingTeam;
import uy.edu.ucu.matchapp.network.RestClient;
import uy.edu.ucu.matchapp.views.adapters.TeamTableListItemView;


public class LeagueTableActivity extends Activity {

    private SoccerSeason mSoccerSeason;
    private LinearLayout mTableLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_season);

        mSoccerSeason = Parcels.unwrap(this.getIntent().getParcelableExtra("SOCCER_SEASON"));
        mTableLinearLayout = (LinearLayout) findViewById(R.id.tableLinearLayout);

        setTitle(mSoccerSeason.getCaption());

        // Fetch league table
        String soccerSeasonUrl = mSoccerSeason.getLinks().get("self").get("href");
        int soccerSeasonId = Integer.parseInt(soccerSeasonUrl.substring(soccerSeasonUrl.lastIndexOf('/') + 1));
        new RestClient(this).getFootballDataService().getSoccerSeasonLeagueTable(soccerSeasonId, new Callback<LeagueTable>() {
            @Override
            public void success(LeagueTable leagueTable, Response response) {
                for(StandingTeam st : leagueTable.getStanding()){
                    TeamTableListItemView item = new TeamTableListItemView(getApplicationContext(),st.getPosition(),st.getTeamName(),st.getPlayedGames(),st.getGoals(),st.getGoalsAgainst(),st.getGoalDifference(),st.getPoints());
                    mTableLinearLayout.addView(item);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                // TODO: Handle error
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_soccer_season, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
