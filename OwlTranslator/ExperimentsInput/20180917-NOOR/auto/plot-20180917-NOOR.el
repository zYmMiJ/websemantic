(TeX-add-style-hook
 "plot-20180917-NOOR"
 (lambda ()
   (setq TeX-command-extra-options
         "--shell-escape")
   (TeX-add-to-alist 'LaTeX-provided-class-options
                     '(("standalone" "multi={tikzpicture}" "convert")))
   (TeX-run-style-hooks
    "latex2e"
    "standalone"
    "standalone10"
    "comment"
    "multirow"
    "pgf"
    "tikz"
    "pgfplots"))
 :latex)

