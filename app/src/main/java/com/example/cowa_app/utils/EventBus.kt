import com.example.cowa_app.data.repository.ToastType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object AppEventBus {
    private val _events = MutableSharedFlow<AppEvent>()
    val events = _events.asSharedFlow()

    suspend fun emit(event: AppEvent) {
        _events.emit(event)
    }

    // 为了方便在非协程环境中使用
    fun emitScope(event: AppEvent) {
        CoroutineScope(Dispatchers.Main).launch {
            _events.emit(event)
        }
    }
}

sealed class AppEvent {
    data class ShowToast(val message: String, val type: ToastType) : AppEvent()
//    data class ShowSnackbar(val message: String) : AppEvent()
//    object TokenExpired : AppEvent()
}