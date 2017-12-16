package fileLock.actions;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.intellij.openapi.project.Project;
import fileLock.config.CurrentAction;
import org.jetbrains.annotations.NotNull;

public class TypedHandler implements TypedActionHandler {
    @Override
    public void execute(@NotNull Editor editor, char c, @NotNull DataContext dataContext) {
        final Document document = editor.getDocument();
        Project project = editor.getProject();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //document.insertString(0, "Typed\n");
                CurrentAction.getOriginalTypedHandler().execute(editor, c, dataContext);
            }
        };
        System.out.print(c);
        WriteCommandAction.runWriteCommandAction(project, runnable);
    }
}
