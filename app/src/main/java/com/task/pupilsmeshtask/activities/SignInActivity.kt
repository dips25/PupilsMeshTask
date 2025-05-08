package com.task.pupilsmeshtask.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.task.pupilsmeshtask.Models.Users
import com.task.pupilsmeshtask.R
import com.task.pupilsmeshtask.Repo.Repo
import com.task.pupilsmeshtask.Utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Matcher
import java.util.regex.Pattern


class SignInActivity:AppCompatActivity() {

    var emailpattern:String = "[A-Za-z]+[.a-zA-Z0-9]{2,}@(gmail|yahoo|outlook|live).com"
    var actionBar:ActionBar?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }


//        actionBar = supportActionBar
//        actionBar!!.setTitle("Sign In")

        Repo.buildDB(this@SignInActivity)

        var edtEmail:EditText = findViewById(R.id.edit_email)
        var edtPassword:EditText = findViewById(R.id.edit_password)
        var btnLogin:TextView = findViewById(R.id.sign_in)

        btnLogin.setOnClickListener {

            if (Utils.isBlank(edtEmail.text.toString())
                || Utils.isBlank(edtPassword.text.toString())) {

                Toast.makeText(this@SignInActivity
                    ,"Invalid email/password"
                    ,Toast.LENGTH_SHORT).show()

                return@setOnClickListener



            }

            val p: Pattern = Pattern.compile(emailpattern)
            val m: Matcher = p.matcher(edtEmail.getText().toString().trim())

            if (m.matches()) {

                CoroutineScope(Dispatchers.IO).launch {

                    val email = edtEmail.text.toString()
                    val passWord = edtPassword.text.toString()

                    val users:Users = Users(email = email, password = passWord)

                    val sUser:Users? = Repo.searchUser(users)

                    if (sUser!=null) {

                        if (sUser.password.equals(passWord)) {

                            val intent = Intent(this@SignInActivity,MainActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)

                            getSharedPreferences("userdetails",Context.MODE_PRIVATE)
                                .edit()
                                .putBoolean("isLoggedIn",true)
                                .commit()


                        } else {

                            runOnUiThread {

                                Toast.makeText(this@SignInActivity
                                    ,"Wrong password entered"
                                    ,Toast.LENGTH_SHORT).show()

                                getSharedPreferences("userdetails",Context.MODE_PRIVATE)
                                    .edit()
                                    .putBoolean("isLoggedIn",false)
                                    .commit()


                            }


                        }


                    } else {

                        Repo.insertUsers(users)

                        val intent = Intent(this@SignInActivity,MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)

                        getSharedPreferences("userdetails",Context.MODE_PRIVATE)
                            .edit()
                            .putBoolean("isLoggedIn",true)
                            .commit()
                    }




                }




            } else {

                runOnUiThread {

                    Toast.makeText(this@SignInActivity
                        ,"Invalid email"
                        ,Toast.LENGTH_SHORT).show()


                }


            }


        }

    }

    override fun onResume() {
        super.onResume()

        if (getSharedPreferences("userdetails",Context.MODE_PRIVATE)
                .getBoolean("isLoggedIn",false)) {

            val intent = Intent(this@SignInActivity,MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }
    }
}