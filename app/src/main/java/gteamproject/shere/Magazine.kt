package gteamproject.shere

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gteamproject.adapters.MagazineRecyclerAdapter

class Magazine: Fragment() {

    // 카드뷰에 사용할 변수 선언
    private var imageList = mutableListOf<Int>()
    private var titleList = mutableListOf<String>()
    private var briefList = mutableListOf<String>()
    private var publisherList = mutableListOf<String>()

    lateinit var recyclerView: RecyclerView

    companion object{
        const val TAG : String = "로그"

        fun newInstance() : Magazine {
            return Magazine()
        }
    }

    // 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(Magazine.TAG, "Magazine_c")
    }

    // 프래그먼트를 안고있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(Magazine.TAG, "Magazine_a")
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 리사이클러뷰 사용
        recyclerView = view!!.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = MagazineRecyclerAdapter(imageList, titleList, briefList, publisherList)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(Magazine.TAG, "Magazine_v")

        postToList()

        val view = inflater.inflate(R.layout.magazine, container, false)

        return view
    }

    // 각각 카드뷰에 등록할 이미지, 제목, 본문, 글쓴이 등록
    private fun addToList(image: Int, title: String, brief: String, publisher: String) {
        imageList.add(image)
        titleList.add(title)
        briefList.add(brief)
        publisherList.add(publisher)
    }

    /**
     * 매거진 페이지에 들어갈 내용들 설정
     * 현재 기능으로는 관리자만 작성할 수 있도록 하여 하드 코딩
     * 매거진 작성 권한은 앞으로도 관리자만 가능하도록 할 것이며
     * 게시물이 많아질 경우 리팩터링 필요!!
     */
    private fun postToList() {

        // 각 배열에 포스트 순서대로 넣어줘야 함
        var titles = arrayOf("첫번째", "두번째", "세번째", "네번째", "다섯번째")
        var contents = arrayOf("", "", "", "", "")
        var publishers = arrayOf("쉐어 편집장")
        var images = arrayOf(R.drawable.paris, R.drawable.moscow, R.drawable.dubai, R.drawable.uk, R.drawable.paris)

        for (i in 0..4) {

            addToList(images[i], "${titles[i]}", "${contents[i]}", "${publishers[0]}")
        }

    }

}