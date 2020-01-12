select coreLabel.name as name
from core_label_metadata coreLabel
join core_user_profile coreUser
on coreLabel.id = coreUser.id
where coreLabel.name = 'IMPORTANT'
and coreUser.user_email_id = 'icbm.iot@gmail.com';


select coreLabel.id as id
from core_label_metadata coreLabel
join core_user_profile coreUser
on coreLabel.id = coreUser.id
where coreLabel.name = 'zzzz'
and coreUser.user_email_id = 'icbm.iot@gmail.com';



select
runManager.id,
runManager.label_filter_id,
runManager.delete_filter_id,
runManager.run_name,
runManager.status,
runManager.failure_reason

from core_run_manager runManager
join label_filter labelFilter
on runManager.label_filter_id = labelFilter.id

join core_label_metadata label
on label.id = labelFilter.label_id

join core_user_profile userProfile
on userProfile.label_id = label.id

where userProfile.user_email_id = 'icbm.iot@gmail.com'
order by runManager.id desc


select
runManager.id,
runManager.label_filter_id,
runManager.delete_filter_id,
runManager.run_name,
runManager.status,
runManager.failure_reason

from core_run_manager runManager
join delete_filter deleteFilter
on runManager.delete_filter_id = deleteFilter.id

join core_label_metadata label
on label.id = deleteFilter.label_id

join core_user_profile userProfile
on userProfile.label_id = label.id

where userProfile.user_email_id = 'icbm.iot@gmail.com'
order by runManager.id desc


--get filter wrapper by filter name--
select

labelFilter.id, 
labelFilter.filter_name,
labelFilter.label_id,
labelFilter.is_email_id_filter,
labelFilter.email_id_keywords,
labelFilter.is_subject_filter,
labelFilter.subject_keywords,
labelFilter.is_body_filter,
labelFilter.body_keywords,
labelFilter.creation_date

from label_filter labelFilter
join core_label_metadata label
on label.id = labelFilter.label_id

join core_user_profile userProfile
on userProfile.label_id = label.id

where userProfile.user_email_id = 'icbm.iot@gmail.com'
and labelFilter.filter_name = 'Label filter by body'
order by labelFilter.id desc;



select

deleteFilter.id, 
deleteFilter.filter_name,
deleteFilter.label_id,
deleteFilter.is_email_id_filter,
deleteFilter.email_id_keywords,
deleteFilter.is_subject_filter,
deleteFilter.subject_keywords,
deleteFilter.is_body_filter,
deleteFilter.body_keywords,
deleteFilter.creation_date

from delete_filter deleteFilter
join core_label_metadata label
on label.id = deleteFilter.label_id

join core_user_profile userProfile
on userProfile.label_id = label.id

where userProfile.user_email_id = 'icbm.iot@gmail.com'
and deleteFilter.filter_name = 'Delete filter by subject'
order by deleteFilter.id desc


select

runManager.id,
runManager.label_filter_id,
runManager.delete_filter_id,
runManager.run_name,
runManager.status,
runManager.failure_reason

from core_run_manager runManager 
join label_filter labelFilter 
on runManager.label_filter_id = labelFilter.id 

join core_label_metadata label
on label.id = labelFilter.label_id

join core_user_profile userProfile
on label.id = userProfile.label_id

where 
labelFilter.filter_name = 'Label filter by body'
and userProfile.user_email_id = 'icbm.iot@gmail.com'
and runManager.runName = ''


delete label_filter
from label_filter

join core_label_metadata
on label_filter.label_id = core_label_metadata.id

join core_user_profile
on core_user_profile.label_id = core_label_metadata.id

where label_filter.id = 4
and core_user_profile.user_email_id = 'info.codeartsolutions@gmail.com';


--Following query need to be changed in RunManagerDBService.getAllRunDetails()--
select 
runManager.id,
runManager.label_filter_id,
runManager.delete_filter_id,
runManager.run_name,
runManager.status,
runManager.failure_reason

from core_run_manager runManager
join label_filter labelFilter
on runManager.label_filter_id = labelFilter.id

join core_label_metadata label
on label.id = labelFilter.label_id

join core_user_profile userProfile
on label.id = userProfile.label_id

where userProfile.user_email_id = 'info.codeartsolutions@gmail.com'

union all

select 

runManager.id,
runManager.label_filter_id,
runManager.delete_filter_id,
runManager.run_name,
runManager.status,
runManager.failure_reason

from core_run_manager runManager
join delete_filter deleteFilter
on deleteFilter.id = runManager.delete_filter_id

join core_label_metadata label
on label.id = deleteFilter.label_id

join core_user_profile userProfile
on label.id = userProfile.label_id

where userProfile.user_email_id = 'info.codeartsolutions@gmail.com';
