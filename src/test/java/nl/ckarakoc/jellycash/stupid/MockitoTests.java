package nl.ckarakoc.jellycash.stupid;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatRuntimeException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class MockitoTests {

  @Test
  public void mockTest() {
    // Arrange
    List mockedList = mock(List.class);

    // Act
    mockedList.add("one");
    mockedList.clear();

    // Assert
    verify(mockedList).add("one");
    verify(mockedList, times(1)).clear();
    verify(mockedList).clear();
  }

  @Test
  public void stubbingTest() {
    LinkedList mockedList = mock(LinkedList.class);

    //stubbing
    when(mockedList.get(0)).thenReturn("first");
    when(mockedList.get(1)).thenThrow(new RuntimeException());

    assertThat(mockedList.get(0)).isEqualTo("first");
    assertThatRuntimeException().isThrownBy(() -> mockedList.get(1));
    assertThat(mockedList.get(999)).isNull();
  }
}
