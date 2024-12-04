import webbrowser
import sys

def start_video_call(classroom_code):
    jitsi_url = f"https://meet.jit.si/{classroom_code}"
    webbrowser.open(jitsi_url)

if __name__ == "__main__":
    if len(sys.argv) > 1:
        classroom_code = sys.argv[1]
        start_video_call(classroom_code)
    else:
        print("Classroom code not provided.")