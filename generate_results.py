#!/usr/bin/env python3

import subprocess
import sys


RESULTS_DIR = 'results'
ALLOY_INTEMED_DIR = 'alloy_intermediates'
ALLOY_TOCHECK_DIR = 'alloy_tocheck'
ALLOY_OUTPUT_DIR = 'TestControllers/Alloy'
RESULTS_FILE = RESULTS_DIR + '/' + 'results.csv'
PROPERTIES_METADATA_FILE = 'TestControllers/controller_metadata.csv'
PROPERTIES_TEXT_DIR = 'properties_text'
EXPECTED_RESULTS_DIR = 'expected_results'
EXPECTED_NEGATIVES_FILE = EXPECTED_RESULTS_DIR + '/expected_negatives.txt'
EXPECTED_POSITIVES_FILE = EXPECTED_RESULTS_DIR + '/expected_positives.txt'
RESULTS_POST_CLASSIFICATION_FILE = RESULTS_DIR + '/results_post_classification.csv'
TABLE_2_COLUMN_SINGLE_FILE = RESULTS_DIR + '/table_2_column_single.csv'
TIMINGS_FILE = RESULTS_DIR + '/timings.csv'

# subprocess.call(['mkdir', '-p', RESULTS_DIR])
# subprocess.call(['mkdir', '-p', ALLOY_INTEMED_DIR])
# subprocess.call(['mkdir', '-p', ALLOY_TOCHECK_DIR])
# subprocess.call(['mkdir', '-p', ALLOY_OUTPUT_DIR])

# results_file = open(RESULTS_FILE, 'w')
properties_meta = open(PROPERTIES_METADATA_FILE)
for prop in properties_meta:
    prop_cln = prop.rstrip('\n')
    columns = prop_cln.split("\",\"")
#     property_id = columns[0].lstrip('"').rstrip('"')
#     print('Checking property for violation: ' + property_id)
    bench_name = columns[0].lstrip('"').rstrip('"')
    controller_name = columns[1].lstrip('"').rstrip('"')
    method_name = columns[2].lstrip('"').rstrip('"')
    controllersig = columns[3].lstrip('"').rstrip('"')

    #print('benchmark_name: ' + benchmark_name)
    #print('benchdir: ' + benchdir)
    #print('controllersig: ' + controllersig)
#     controllersig_split = controllersig.split(':')
#     upto_controller_class = controllersig_split[0]
    #print('upto_controller_class: ' + upto_controller_class)
#     controller_name_with_params = controllersig_split[1]
    #print('controller_name_with_params: ' + controller_name_with_params)
#     before_controller_class_dot_idx = upto_controller_class.rindex('.')
#     class_name = upto_controller_class[before_controller_class_dot_idx + 1 : ]
#     controller_name = controller_name_with_params[ : controller_name_with_params.index('(')]
    outdir = ALLOY_OUTPUT_DIR
    subprocess.call(['mkdir', '-p', outdir])
    outfile = outdir + '/' + bench_name + '_' + controller_name +'_' + method_name  + '.als'
    veMapOutDir = 'TestControllers/VeMap'
    veMapOutFile = veMapOutDir+'/'+bench_name+'_'+controller_name+'_'+method_name +'.txt'
    bench_dir = 'target/classes'
    repo = ""
#     if (len(columns) == 5):
#         repo = columns[4].lstrip('"').rstrip('"')
    runline = ['java', '-jar', 'out/artifacts/eqsql_jar/eqsql.jar', '-benchdir', bench_dir,'-controllersig']
    runline.append(controllersig)
    runline.append('-outfile')
    runline.append(outfile)
    # runline.append('>>')
    # runline.append(veMapOutFile)
#     if (repo != ""):
#         runline.append('-repo')
#         runline.append(repo)
#     start_summary_inf = timer()
    with open(veMapOutFile, 'a') as f_output:
        # subprocess.check_output(runline)
        subprocess.Popen(runline, stdout=f_output)
#     end_summary_inf = timer()

#     time_elp_summary = end_summary_inf - start_summary_inf
#     result_time_summary= "{:.2f}".format(time_elp_summary) + 's'

#     alloyfile = open(outfile,mode='r')
#     alloy_model_lines = alloyfile.readlines()
#     alloyfile.close()
#
#     assertion_file_name = PROPERTIES_TEXT_DIR + '/' + property_id + ".txt"
#     assertionfile = open(assertion_file_name, mode='r')
#     assertion_text_lines = assertionfile.readlines()
#     assertionfile.close()
#
#     alloy_tocheck_lines = alloy_model_lines + assertion_text_lines
#     alloy_tocheck_filename = ALLOY_TOCHECK_DIR + '/' + property_id + '.als'
#     alloy_output_filename  = ALLOY_OUTPUT_DIR + '/' + property_id + '.txt'
#     alloy_tocheck_file = open(alloy_tocheck_filename, 'w')
#     alloy_tocheck_file.writelines(alloy_tocheck_lines)
#     alloy_tocheck_file.close()
#
#     alloy_run_line = ['java', '-jar', 'alloyrun.jar', alloy_tocheck_filename, alloy_output_filename]
#     alloy_out = subprocess.check_output(alloy_run_line).decode(sys.stdout.encoding)
#     alloy_out_split = alloy_out.split(' ')
#     result_alloy_out_result = alloy_out_split[0].strip()
#     result_alloy_out_time = alloy_out_split[1].strip()
#
#     result_row_out = '"' + property_id + "\",\"" + result_alloy_out_result + "\",\"" + result_time_summary + "\",\"" + result_alloy_out_time + "\",\"" + controllersig  + "\",\"" + benchmark_name + '"'
#     print(result_alloy_out_result)
#     results_file.write(result_row_out + '\n')

# results_file.close()
properties_meta.close()

