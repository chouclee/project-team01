package json.gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import com.github.julman99.gsonfire.GsonFireBuilder;
import com.github.julman99.gsonfire.TypeSelector;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class TestYesNoSet {
  public static final class QuestionTypeSelector implements TypeSelector<TestYesNoQuestion> {
    @Override
    public Class<? extends TestYesNoQuestion> getClassForElement(JsonElement readElement) {
      return TestYesNoQuestion.class;
    }
  }

  private static Gson gson = new GsonFireBuilder().registerTypeSelector(TestYesNoQuestion.class,
          new QuestionTypeSelector()).createGson();

  private List<? extends TestYesNoQuestion> questions;

  public TestYesNoSet(List<? extends TestYesNoQuestion> answers) {
    this.questions = answers;
  }

  public static List<? extends TestYesNoQuestion> load(Reader reader) {
    TestYesNoSet input = gson.fromJson(reader, TestYesNoSet.class);
    return input.questions;
  }

  public static List<? extends TestYesNoQuestion> load(InputStream stream) {
    return load(new InputStreamReader(stream));
  }

  public static String dump(List<? extends TestYesNoQuestion> answers) {
    TestYesNoSet output = new TestYesNoSet(answers);
    return gson.toJson(output, TestYesNoSet.class);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((questions == null) ? 0 : questions.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TestYesNoSet other = (TestYesNoSet) obj;
    if (questions == null) {
      if (other.questions != null)
        return false;
    } else if (!questions.equals(other.questions))
      return false;
    return true;
  }
}