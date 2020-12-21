package rural.rhiss.Login;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.droidbyme.dialoglib.AnimUtils;
import com.droidbyme.dialoglib.DroidDialog;

import rural.rhiss.BeneficiaryLogin.BeneficiaryLoginActivity;
import rural.rhiss.LGDCode.GetLGDCodeActivity;
import rural.rhiss.PLI.PLILoginActivity;
import rural.rhiss.R;
import rural.rhiss.Util.GlobalLocationService;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

public class LoginActivity extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener*/ {
    public Toolbar mToolbar;
    private LinearLayout PLILoginLinearLayout, beneficiaryLoginLinearLayout, getLGDCodeLinearLayout;
    NavigationView navigationView;
    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private boolean mChangeFragment;
    private int selectedItem;
    private static final int PERMISSION_REQUEST_CODE = 200;
    CardView pli_login, beneficiary_login, location_code, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        PLILoginLinearLayout = findViewById(R.id.linearPLILogin);
        beneficiaryLoginLinearLayout = findViewById(R.id.linearBeneficiaryLogin);
        getLGDCodeLinearLayout = findViewById(R.id.linearGetLGDCode);

        pli_login = findViewById(R.id.pli_ligin);
        beneficiary_login = findViewById(R.id.beneficary_login);
        location_code = findViewById(R.id.location_code);
        logout = findViewById(R.id.logout);

        if (!checkPermission()) {
            requestPermission();
        }

        setupToolbar();
        //setupNavigationView();
        onClick();
    }

    private void onClick() {
        pli_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PLILoginActivity.class);
                startActivity(intent);
            }
        });

        beneficiary_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BeneficiaryLoginActivity.class);
                startActivity(intent);
            }
        });

        location_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GetLGDCodeActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DroidDialog.Builder(LoginActivity.this)
                        .icon(R.drawable.info_icon)
                        .title(getString(R.string.exit))
                        .content(getString(R.string.exit_text))
                        .cancelable(true, true)
                        .positiveButton(getString(R.string.ok), new DroidDialog.onPositiveListener() {
                            @Override
                            public void onPositive(Dialog droidDialog) {
                                finish();
                            }
                        })

                        .negativeButton("CANCEL", new DroidDialog.onNegativeListener() {
                            @Override
                            public void onNegative(Dialog droidDialog) {
                                droidDialog.dismiss();
                            }
                        })
                        .color(ContextCompat.getColor(LoginActivity.this, R.color.app_color), ContextCompat.getColor(LoginActivity.this, R.color.white),
                                ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary))
                        .animation(AnimUtils.AnimFadeInOut)
                        .show();

            }
        });

    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_SMS);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), GET_ACCOUNTS);
        int result4 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result5 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && result5 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, READ_SMS, Manifest.permission.CAMERA, GET_ACCOUNTS, READ_EXTERNAL_STORAGE, READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean cameraAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                boolean readSMS = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                boolean getAccounts = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                boolean externalStorage = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                boolean phoneState = grantResults[6] == PackageManager.PERMISSION_GRANTED;

                if (locationAccepted && cameraAccepted && readSMS && getAccounts && externalStorage && phoneState) {
                    startService(new Intent(LoginActivity.this, GlobalLocationService.class));

                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("This permission is important for application.")
                                .setTitle("Important permission required");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, READ_SMS, Manifest.permission.CAMERA, GET_ACCOUNTS, READ_PHONE_STATE},
                                            PERMISSION_REQUEST_CODE);
                                }
                            }
                        });
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, READ_SMS, Manifest.permission.CAMERA, GET_ACCOUNTS, READ_PHONE_STATE},
                                    PERMISSION_REQUEST_CODE);
                        }
                    } else {
                    }
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        new DroidDialog.Builder(LoginActivity.this)
                .icon(R.drawable.info_icon)
                .title(getString(R.string.exit))
                .content(getString(R.string.exit_text))
                .cancelable(true, true)
                .positiveButton(getString(R.string.ok), new DroidDialog.onPositiveListener() {
                    @Override
                    public void onPositive(Dialog droidDialog) {
                        finish();
                    }
                })

                .negativeButton("CANCEL", new DroidDialog.onNegativeListener() {
                    @Override
                    public void onNegative(Dialog droidDialog) {
                        droidDialog.dismiss();
                    }
                })
                .color(ContextCompat.getColor(LoginActivity.this, R.color.app_color), ContextCompat.getColor(LoginActivity.this, R.color.white),
                        ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary))
                .animation(AnimUtils.AnimFadeInOut)
                .show();
    }


    /*private void setupNavigationView()
    {
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerLayout.setScrimColor(Color.parseColor("#99000000"));
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView)
            {
                if (mChangeFragment)
                {
                    FragmentManager fragmentManager = getFragmentManager();
                    mChangeFragment = false;
                    switch (selectedItem)
                    {
                        case R.id.menu_startpage_programs:

                            break;
                        case R.id.menu_startpage_qrcode:

                            break;
                        case R.id.menu_startpage_language:

                            break;
                        case R.id.menu_drawer_startpage_contactus:

                            break;
                        case R.id.menu_startpage_about_us:

                            break;


                    }
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
                Utils.hideSoftKeyboard(LoginActivity.this);
            }
        };
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem drawerItem) {
        drawerItem.setChecked(!drawerItem.isChecked()); // set selected state in navigation item
        selectedItem = drawerItem.getItemId();
        mDrawerLayout.closeDrawers(); // Closes drawer as soon as item is clicked
        mChangeFragment = true;
        return true;
    }
    @Override
    public void onBackStackChanged() {
        int mBackStackCount = getFragmentManager().getBackStackEntryCount();

        if (mBackStackCount > 0) {

        }
    }*/
}