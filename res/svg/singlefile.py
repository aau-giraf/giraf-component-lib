import os, sys
import shutil
import subprocess
import argparse

def run(argument):
	filename, ext = os.path.splitext(argument)
	if ("svg" in ext):
		args = ["inkscape", "-z", "-e",
			os.path.join("..", "drawable-ldpi", filename+".png"),
			"-w", "36", "-h", "36", argument]
		p = subprocess.Popen(args)
		p.communicate()
		args = ["inkscape", "-z", "-e",
			os.path.join("..", "drawable-mdpi", filename+".png"),
			"-w", "48", "-h", "48", argument]
		p = subprocess.Popen(args)
		p.communicate()
		args = ["inkscape", "-z", "-e",
			os.path.join("..", "drawable-hdpi", filename+".png"),
			"-w", "72", "-h", "72", argument]
		p = subprocess.Popen(args)
		p.communicate()
		args = ["inkscape", "-z", "-e",
			os.path.join("..", "drawable-xhdpi", filename+".png"),
			"-w", "96", "-h", "96", argument]
		p = subprocess.Popen(args)
		p.communicate()
	sys.exit("Done")

def main():
	parser = argparse.ArgumentParser()
	parser.add_argument('path', metavar='p', type=str, default=None, help='Saet stien ??')
	args = parser.parse_args()

	run(args.path)

if __name__ == '__main__':
	main()
