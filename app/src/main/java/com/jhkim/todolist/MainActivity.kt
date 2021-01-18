package com.jhkim.todolist

import android.graphics.Paint
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jhkim.todolist.databinding.ActivityMainBinding
import com.jhkim.todolist.databinding.ItemTodoBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val data = arrayListOf<Todo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        data.add(Todo("숙제", false))
        data.add(Todo("청소", false))


        binding.recyclerView.apply{
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = TodoAdapter(data,
                onClickDeleteIcon = {
                    deleteTodo(it)
                },
                onClickItem = {
                    toggleTodo(it)
                }
            )
        }

        binding.addButton.setOnClickListener {
            addTodo()
        }

    }

    private fun toggleTodo(todo: Todo) {
        todo.isDone = !todo.isDone
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun addTodo() {
        val todo = Todo(binding.editText.text.toString())
        data.add(todo)//추가 버튼 눌렀을 때 수행
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun deleteTodo(todo: Todo) {
        data.remove(todo)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}

data class Todo(
    val text: String,
    var isDone: Boolean = false
)

class TodoAdapter(
    private val dataSet: List<Todo>,
    val onClickDeleteIcon: (todo: Todo) -> Unit,
    val onClickItem: (todo: Todo) -> Unit

) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(ItemTodoBinding.bind(view))
    }

    //아이템을 화면에 표시
    override fun onBindViewHolder(viewHolder: TodoViewHolder, position: Int) {
        val todo = dataSet[position]
        viewHolder.binding.todoText.text = todo.text

        if(todo.isDone){
//            viewHolder.binding.todoText.paintFlags = viewHolder.binding.todoText.paintFlags or
//                    Paint.STRIKE_THRU_TEXT_FLAG
            viewHolder.binding.todoText.apply{
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                setTypeface(null, Typeface.ITALIC)
            }
        }else{
            viewHolder.binding.todoText.apply{
                paintFlags = 0
                setTypeface(null,Typeface.NORMAL)
            }


        }
        viewHolder.binding.deleteImageView.setOnClickListener {
            onClickDeleteIcon.invoke(todo)

        }
        viewHolder.binding.root.setOnClickListener {
            onClickItem.invoke(todo)
        }
    }

    override fun getItemCount() = dataSet.size

}
