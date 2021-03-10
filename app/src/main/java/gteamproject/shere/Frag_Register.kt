package gteamproject.shere

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.regex.Pattern

class Frag_Register : Fragment() {

    lateinit var email : EditText
    lateinit var userName2 : EditText
    lateinit var password2 : EditText
    lateinit var chkAgree : CheckBox
    lateinit var btnSignup : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_regester, container, false)


        email = view.findViewById(R.id.email)
        userName2 = view.findViewById(R.id.userName2)
        password2 = view.findViewById(R.id.password2)
        chkAgree = view.findViewById(R.id.chkAgree)
        btnSignup = view.findViewById(R.id.btnSignup)


        btnSignup.setOnClickListener {
            val inputEmail = email.text.trim().toString()
            val inputName = userName2.text.trim().toString()
            val inputPassword = password2.text.trim().toString()
            val inputChk = chkAgree.isChecked


            if(inputEmail.equals(email) && inputName.equals(userName2) && inputPassword.equals(password2) && inputChk.equals(chkAgree)) {
                val intent = Intent(activity, Main::class.java)
                startActivity(intent)
            }
            else {
                if (inputEmail.isNullOrEmpty()) {
                    Toast.makeText(activity, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    email.requestFocus()
                    return@setOnClickListener
                }

                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()){
                    Toast.makeText(activity, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                    email.requestFocus()
                    return@setOnClickListener
                }

                if (inputName.isNullOrEmpty()){
                    Toast.makeText(activity, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    userName2.requestFocus()
                    return@setOnClickListener
                }

                if (inputPassword.isNullOrEmpty()){
                    Toast.makeText(activity, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    password2.requestFocus()

                    return@setOnClickListener
                }

                if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", inputPassword)){
                    Toast.makeText(activity,"비밀번호는 영어,숫자,특수문자 포함 8-20자로 입력해주세요.",Toast.LENGTH_SHORT).show()
                    password2.requestFocus()

                    return@setOnClickListener
                }

                if(!inputChk ){
                    Toast.makeText(activity, "이용약관에 동의해 주세요.", Toast.LENGTH_SHORT).show()

                    return@setOnClickListener
                }

                //TODO : SQL LITE 사용 하여 검증 로직 추가
                //Toast.makeText(activity, "이미 가입된 회원정보 입니다.", Toast.LENGTH_SHORT).show()

            }
        }

        return view
    }


}