package com.example.happy934.tempideabox;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Idea Box");

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setUpViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }
    private void setUpViewPager(ViewPager viewPager){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new NewIdea(),"New idea");
        viewPagerAdapter.addFragment(new TripToMyIdea(),"Trip to my idea");
        viewPager.setAdapter(viewPagerAdapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{
        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> fragmentListTitle = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }

        public Fragment getItem(int position){
            return fragmentList.get(position);
        }

        public int getCount(){
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment,String title){
            fragmentList.add(fragment);
            fragmentListTitle.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position){
            return fragmentListTitle.get(position);
        }
    }
}
