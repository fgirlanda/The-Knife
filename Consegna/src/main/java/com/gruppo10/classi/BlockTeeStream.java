package com.gruppo10.classi;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * {@code BlockTeeStream} è una classe che estende {@link OutputStream} e permette
 * di duplicare l'output scrivendo contemporaneamente su due stream distinti:
 * uno per la console e uno per un file.
 * 
 * <p>
 * La classe aggiunge un timestamp all'inizio di ogni nuovo blocco di testo (una sequenza di caratteri terminata da newline),
 * mentre le righe successive dello stesso blocco vengono scritte senza timestamp.
 * Tutti i dati vengono flushati immediatamente sia sulla console sia sul file.
 * </p>
 */
public class BlockTeeStream extends OutputStream {

    /** Stream su cui scrivere l'output della console. */
    private final OutputStream console;

    /** Stream su cui scrivere l'output del file di log. */
    private final OutputStream file;

    /** Buffer interno che accumula i caratteri fino al newline. */
    private final StringBuilder buffer = new StringBuilder();

    /** Indica se il prossimo blocco deve avere il timestamp iniziale. */
    private boolean isNewBlock = true;

    /** Formato del timestamp usato all'inizio di ogni blocco. */
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Costruisce un nuovo {@code BlockTeeStream}.
     *
     * @param console lo stream su cui scrivere l'output della console
     * @param file lo stream su cui scrivere l'output del file di log
     */
    public BlockTeeStream(OutputStream console, OutputStream file) {
        this.console = console;
        this.file = file;
    }

    /**
     * Scrive un singolo byte nello stream.
     * <p>
     * Accumula i caratteri in un buffer interno fino a incontrare un newline ('\n').
     * Quando viene rilevato un newline:
     * <ul>
     *     <li>Se è l'inizio di un nuovo blocco, aggiunge un timestamp prima del testo.</li>
     *     <li>Scrive il contenuto sia sullo stream console sia sullo stream file.</li>
     *     <li>Esegue il flush immediato di entrambi gli stream.</li>
     * </ul>
     * </p>
     *
     * @param b il byte da scrivere
     * @throws IOException se si verifica un errore di I/O sugli stream sottostanti
     */
    @Override
    public void write(int b) throws IOException {
        if (b == '\n') {
            if (buffer.length() > 0) {
                String line;
                if (isNewBlock) {
                    String ts = LocalDateTime.now().format(fmt);
                    line = "[" + ts + "] " + buffer + System.lineSeparator();
                    isNewBlock = false;
                } else {
                    line = buffer + System.lineSeparator();
                }

                byte[] bytes = line.getBytes();

                console.write(bytes);
                console.flush();

                file.write(bytes);
                file.flush();

                buffer.setLength(0);
            } else {
                isNewBlock = true;
            }
        } else {
            buffer.append((char) b);
        }
    }
}