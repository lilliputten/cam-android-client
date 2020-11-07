/** @desc Update build date/time tag file with current timestamp
 *  @changed 2020.05.26, 17:12
 */

const fs = require('fs')
const dateformat = require('dateformat')

const now = new Date()
const buildTag = dateformat(now, 'yymmdd-HHMM')
const buildTime = dateformat(now, 'yyyy.mm.dd, HH:MM')

console.log('Updating build tag/time: ' + buildTag + ' / ' + buildTime)

fs.writeFileSync('build-timetag.txt', buildTag, 'utf8')
fs.writeFileSync('build-timestamp.txt', buildTime, 'utf8')
