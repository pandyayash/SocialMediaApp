package technource.autoreum.activities;

import static technource.autoreum.activities.LoginScreen.FLAG_SIGNUP;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import technource.autoreum.CustomViews.Widgets.CustomViewPager;
import technource.autoreum.R;
import technource.autoreum.fragment.fragmentRegisterAccount;
import technource.autoreum.fragment.fragmentRegisterCar;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.SignUpDBO;

/**
 * Created by technource on 6/9/17.
 */

public class SignUpCarOwner extends BaseActivity implements OnClickListener {

  private final static String CONDENSED_FONT = "poppins_medium.TTF";
  private static final Integer FACEBOOK = 1;
  private static final Integer GOOGLE_PLUS = 2;
  public static int current_position = 0;
  public static boolean isClickedTab = false;
  public String TAG = "SignUp Car Owner";
  public TabLayout tabLayout;
  public CustomViewPager viewPager;
  public SignUpDBO data;
  public ImageView ll_back;
  public TextView tv_skipCarSign;
  public String Email, Password;
  Context appContext;
  LoginDetail_DBO loginDetail_dbo;
  ViewPagerAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.signup_carowner);
    getViews();
  }

  public void getViews() {
    appContext = this;
    loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
    viewPager = findViewById(R.id.viewpager);
    setupViewPager(viewPager);
    data = new SignUpDBO();
    viewPager.setPagingEnabled(false);
    tabLayout = findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(viewPager);
    changeTabsFont();
    ll_back = findViewById(R.id.ll_back);
    tv_skipCarSign = findViewById(R.id.tv_skipCarSign);
    tv_skipCarSign.setVisibility(View.GONE);

    getData();
    setOnClickListener();

    if (isClickedTab == false) {
      DisableTabLAyoutClick();
    }
  }


  public void getData() {
    Intent intent = getIntent();
    if (intent != null) {
      data = intent.getParcelableExtra("data");
    }
  }

  public void setOnClickListener() {
    ll_back.setOnClickListener(this);
    tv_skipCarSign.setOnClickListener(this);
  }

  private void setupViewPager(ViewPager viewPager) {
    adapter = new ViewPagerAdapter(getSupportFragmentManager());
    adapter.addFragment(new fragmentRegisterAccount(), getString(R.string.resgister_account));
    adapter.addFragment(new fragmentRegisterCar(), getString(R.string.register_car));
    viewPager.setAdapter(adapter);
  }

  public void DisableTabLAyoutClick() {
    LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
    tabStrip.setEnabled(false);
    for (int i = 0; i < tabStrip.getChildCount(); i++) {
      tabStrip.getChildAt(i).setClickable(false);
    }

  }

  private void changeTabsFont() {
    ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
    int tabsCount = vg.getChildCount();
    for (int j = 0; j < tabsCount; j++) {
      ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
      int tabChildsCount = vgTab.getChildCount();
      for (int i = 0; i < tabChildsCount; i++) {
        View tabViewChild = vgTab.getChildAt(i);
        if (tabViewChild instanceof TextView) {
          Typeface tf = Typeface.createFromAsset(getAssets(), CONDENSED_FONT);
          ((TextView) tabViewChild).setTypeface(tf);
        }
      }
    }
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.ll_back:
        onBackPressed();
        break;
      case R.id.tv_skipCarSign:
        Fragment fr = adapter.getItem(1);

        if (FLAG_SIGNUP.equalsIgnoreCase("1")) {
          if (fr instanceof fragmentRegisterCar) {
            ((fragmentRegisterCar) fr).LoginCheck();
          }
        } else {
          if (fr instanceof fragmentRegisterCar) {
            ((fragmentRegisterCar) fr).SocialLoginCheck();
          }
        }

        break;
    }
  }

  @Override
  public void onBackPressed() {
    if (current_position == 0) {
      finish();
      activityTransition();
      isClickedTab = false;
    } else if (current_position == 1) {
      Fragment fr = adapter.getItem(0);
      if (fr instanceof fragmentRegisterAccount) {
        ((fragmentRegisterAccount) fr).setPersonalLayout();
      }
    } else if (current_position == 2) {
      Fragment fr = adapter.getItem(0);
      if (fr instanceof fragmentRegisterAccount) {
        ((fragmentRegisterAccount) fr).setUserLayout();
      }
    } else if (current_position == 3) {

    }
  }

  class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();


    public ViewPagerAdapter(FragmentManager manager) {
      super(manager);
    }

    @Override
    public Fragment getItem(int position) {
      return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
      return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
      mFragmentList.add(fragment);
      mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return mFragmentTitleList.get(position);
    }
  }
}
