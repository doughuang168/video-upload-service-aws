package com.doughuang168.video.service.mediacontainer;

import io.humble.video.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ContainerInfoService {
    /**
     * A value that means no time stamp is set for a given object.<br>
     * if the Media#getTimeStamp() method of an<br>
     * object returns this value it means the time stamp wasn't set.
     */
    public final  long NO_PTS = VideoJNI.Global_NO_PTS_get();
    /**
     * The default time units per second that we use for decoded<br>
     * MediaRaw objects.<br>
     * <br>
     * This means that 1 tick of a time stamp is 1 Microsecond.
     */
    public final  long DEFAULT_PTS_PER_SECOND = VideoJNI.Global_DEFAULT_PTS_PER_SECOND_get();

    public String GetBasicDuration(String file) throws InterruptedException, IOException {

        final Demuxer demuxer = Demuxer.make();

        // We open the demuxer by pointing it at a URL.
        demuxer.open(file, null, false, true, null, null);

        final String formattedDuration = getTimeStamp(demuxer.getDuration());
        StringBuilder sb = new StringBuilder();
        sb.append(formattedDuration);
        sb.append(",");
        sb.append(" bitrate: ");
        sb.append(demuxer.getBitRate()/1000);
        sb.append(" kb/s");

        demuxer.close();//Don't hold the handle
        return sb.toString();
    }

    private String getTimeStamp(long duration) {
        if (duration == Global.NO_PTS) {
            return "00:00:00.00";
        }
        double d = 1.0 * duration / Global.DEFAULT_PTS_PER_SECOND;
        int hours = (int) (d / (60*60));
        int mins = (int) ((d - hours*60*60) / 60);
        int secs = (int) (d - hours*60*60 - mins*60);
        int subsecs = (int)((d - (hours*60*60.0 + mins*60.0 + secs))*100.0);
        return String.format("%1$02d:%2$02d:%3$02d.%4$02d", hours, mins, secs, subsecs);
    }


}
