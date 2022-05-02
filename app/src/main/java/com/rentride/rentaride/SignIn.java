package com.rentride.rentaride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignIn extends AppCompatActivity {
    AppCompatButton SignInButton;
    TextView TermsConditions;
    private GoogleSignInClient signInClient;
    private final static int RC_SIGN_IN= 123;
    private FirebaseAuth auth;
    String[] email = new String[] {"calebmasake69@gmail.com","nastymasake@gmail.com"};
    List<String> emailList = new ArrayList<>(Arrays.asList(email));

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null){
            Intent intent = new Intent(getApplicationContext(),Home.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        SignInButton = findViewById(R.id.SignInBtn);
        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInUser();
            }
        });
        auth = FirebaseAuth.getInstance();

        TermsConditions = findViewById(R.id.termsConditions);
        TermsConditions.setMovementMethod(LinkMovementMethod.getInstance());

        String value = "<html><font color=#757b86><b><a href=\"https://pages.flycricket.io/rentaride/terms.html\">Terms and Conditions</a></b></font> </html>";
        Spannable spannedText = (Spannable)
                Html.fromHtml(value);

        Spannable processedText = removeUnderlines(spannedText);
        TermsConditions.setText(processedText);

        createSignInRequest();
    }


    private void createSignInRequest() {
        // Configure sign-in to request the user's ID, email address, and basic  profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //build a google signInClient
        signInClient = GoogleSignIn.getClient(this,gso);
    }

    @SuppressWarnings("deprecation")
    public void SignInUser(){
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Signed in successfully, show authenticated UI.
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();
                    Intent intent = new Intent(getApplicationContext(),Home.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(SignIn.this, "something wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public static Spannable removeUnderlines(Spannable p_Text) {
        URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            p_Text.setSpan(span, start, end, 0);
        }
        return p_Text;
    }
}