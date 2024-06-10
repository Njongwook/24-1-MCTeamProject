import android.graphics.Color
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class SalarydayDecorator(private val salaryDay: CalendarDay) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day != null && day == salaryDay
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(ForegroundColorSpan(Color.RED))
        view?.addSpan(StyleSpan(android.graphics.Typeface.BOLD))
        view?.addSpan(RelativeSizeSpan(1.2f))
    }
}
